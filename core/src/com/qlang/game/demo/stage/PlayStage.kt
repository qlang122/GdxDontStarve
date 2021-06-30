package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
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


class PlayStage : Stage {
    private val mainManager: AssetManager? = GameManager.instance?.mainManager
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private var sceneLoader: SceneLoader? = null
    private var wrapper: ItemWrapper? = null

    constructor() : super(ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)) {
        mainManager?.let { mgr ->
            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.injectExternalItemType(SpriterItemType())
            ComponentRetriever.addMapper(PlayerComponent::class.java)

            val cameraSystem = CameraSystem(5f, 40f, 5f, 6f)
            sceneLoader?.engine?.let { engine ->
                engine.addSystem(PlayerAnimationSystem())
                engine.addSystem(cameraSystem)
            }

            sceneLoader?.loadScene("PlayScene", viewport)

            wrapper = ItemWrapper(sceneLoader?.root)

            sceneLoader?.engine?.let { engine ->
                val player = wrapper?.getChild("player")
                player?.addScript(PlayerScript(engine), engine)
                player?.entity?.add(engine.createComponent(PlayerComponent::class.java))
                cameraSystem.setFocus(player?.entity)

                wrapper?.getChild("dragonfly")?.addScript(DragonflyScript(engine), engine)
            }
        }
    }

    fun resize(width: Int, height: Int) {
        if (width != 0 && height != 0) sceneLoader?.resize(width, height)
    }

    override fun draw() {
        super.draw()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        super.dispose()
        sceneLoader?.dispose()
    }
}