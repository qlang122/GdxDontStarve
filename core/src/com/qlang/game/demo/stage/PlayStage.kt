package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.PerspectiveCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.script.DragonflyScript
import com.qlang.game.demo.script.PlayerScript
import com.qlang.game.demo.system.CameraSystem
import com.qlang.game.demo.system.PlayerAnimationSystem
import com.qlang.h2d.extention.spriter.SpriterItemType
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper
import kotlin.math.abs


class PlayStage : Stage {
    private val mainManager: AssetManager? = GameManager.instance?.mainManager
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private var sceneLoader: SceneLoader? = null
    private var wrapper: ItemWrapper? = null

    private var playCamera: Camera? = null
    private var playViewport: Viewport? = null

    constructor() : super(ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)) {
        mainManager?.let { mgr ->
            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.injectExternalItemType(SpriterItemType())

            val cameraSystem = CameraSystem(-10000f, 10000f, -10000f, 10000f)
            sceneLoader?.engine?.let { engine ->
                engine.addSystem(PlayerAnimationSystem())
                engine.addSystem(cameraSystem)
            }
            ComponentRetriever.addMapper(PlayerComponent::class.java)

//            playCamera = OrthographicCamera(AppConfig.worldWidth, AppConfig.worldHeight)
            playCamera = PerspectiveCamera(15f, AppConfig.worldWidth, AppConfig.worldHeight)
            playViewport = ExtendViewport(AppConfig.worldWidth, AppConfig.worldHeight, playCamera)
            sceneLoader?.loadScene("PlayScene", playViewport)

            wrapper = ItemWrapper(sceneLoader?.root)

            sceneLoader?.engine?.let { engine ->
                val player = wrapper?.getChild("player")
                player?.entity?.add(engine.createComponent(PlayerComponent::class.java))
                player?.addScript(PlayerScript(engine), engine)
                cameraSystem.setFocus(player?.entity)

                wrapper?.getChild("dragonfly")?.addScript(DragonflyScript(engine), engine)
            }
        }
    }

    fun resize(width: Int, height: Int) {
        playViewport?.update(width, height)

        if (width != 0 && height != 0) sceneLoader?.resize(width, height)
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