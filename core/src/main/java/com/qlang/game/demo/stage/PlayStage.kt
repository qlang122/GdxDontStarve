package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.component.ScaleEntityComponent
import com.qlang.game.demo.component.WorldComponent
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.script.DragonflyScript
import com.qlang.game.demo.script.PlayerBodyScript
import com.qlang.game.demo.script.PlayerScript
import com.qlang.game.demo.script.WorldScript
import com.qlang.game.demo.system.CameraSystem
import com.qlang.game.demo.system.ChangeVisionSystem
import com.qlang.game.demo.system.EntitySystem
import com.qlang.h2d.extention.spriter.SpriterItemType
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class PlayStage : Stage {
    private val mainManager: AssetManager? = GameManager.instance?.mainManager
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private var sceneLoader: SceneLoader? = null
    private var wrapper: ItemWrapper? = null

    private var playCamera: Camera? = null
    private var playViewport: Viewport? = null

    private val changeVisionSystem = ChangeVisionSystem()

    constructor() : super(ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)) {
        mainManager?.let { mgr ->
            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.injectExternalItemType(SpriterItemType())

            val cameraSystem = CameraSystem(-10000f, 10000f, -10000f, 10000f)

            sceneLoader?.engine?.let { engine ->
                engine.addSystem(EntitySystem())
                engine.addSystem(changeVisionSystem)
                engine.addSystem(cameraSystem)
            }
            ComponentRetriever.addMapper(WorldComponent::class.java)
            ComponentRetriever.addMapper(PlayerComponent::class.java)
            ComponentRetriever.addMapper(ScaleEntityComponent::class.java)
            ComponentRetriever.addMapper(EntityComponent::class.java)
            changeVisionSystem.setViewportHeight(AppConfig.worldHeight)

            playCamera = OrthographicCamera(AppConfig.worldWidth, AppConfig.worldHeight)
            playViewport = ExtendViewport(AppConfig.worldWidth, AppConfig.worldHeight, playCamera)
            sceneLoader?.loadScene("PlayScene", playViewport)
            sceneLoader?.addComponentByTagName("scaleEntity", ScaleEntityComponent::class.java)
            sceneLoader?.addComponentByTagName("entity", EntityComponent::class.java)

            wrapper = ItemWrapper(sceneLoader?.root)

            sceneLoader?.engine?.let { engine ->
                wrapper?.entity?.add(engine.createComponent(WorldComponent::class.java))
                wrapper?.addScript(WorldScript(), engine)

                val player = wrapper?.getChild("player")
                player?.getChild("role")?.entity?.add(engine.createComponent(PlayerComponent::class.java))
                player?.addScript(PlayerScript(engine), engine)
                player?.addScript(PlayerBodyScript().apply { setRootEntity(wrapper?.entity) }, engine)
                cameraSystem.setFocus(player?.entity)
                changeVisionSystem.setPlayer(player?.entity)

                wrapper?.getChild("dragonfly")?.apply {
                    addScript(DragonflyScript(engine), engine)
//                    entity?.getComponent(MainItemComponent::class.java)?.visible = false
                }
            }
        }
    }

    fun resize(width: Int, height: Int) {
        playViewport?.update(width, height)

        if (width != 0 && height != 0) sceneLoader?.resize(width, height)
//        changeVisionSystem.setViewportHeight(height.plus(0f))
    }

    override fun draw() {
        super.draw()
        viewport.apply()
        playViewport?.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        super.dispose()
        sceneLoader?.dispose()
    }
}