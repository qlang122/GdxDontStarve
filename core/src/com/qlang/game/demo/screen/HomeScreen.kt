package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.stage.ExitAppDialog
import com.qlang.game.demo.stage.HomeMenuStage
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager

class HomeScreen : ScreenAdapter() {
    private var bgStage: Stage? = null
    private var menuStage: Stage? = null
    private var exitDialog: ExitAppDialog? = null

    private var sceneLoader: SceneLoader? = null
    private lateinit var viewport: Viewport

    init {
        menuStage = HomeMenuStage().apply {
            setOnItemClickListener { navigation2Menu(it) }
        }

        viewport = ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)
        GameManager.instance?.mainManager?.let {
            sceneLoader = SceneLoader(it.get("project.dt", AsyncResourceManager::class.java))
//            sceneLoader?.injectExternalItemType(SpriterItemType())
            sceneLoader?.loadScene("MainScene", viewport)
        }
    }

    private fun navigation2Menu(index: Int) {
        when (index) {
            0 -> {
            }
            1 -> {
                Navigator.push(WorlListScreen())
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
            5 -> {
                exitDialog = ExitAppDialog().apply {
                    setOnClickListener({ Gdx.app.exit() }, { hide() })
                }
                menuStage?.let { exitDialog?.show(it) }
            }
        }
    }

    override fun show() {
        super.show()
        menuStage?.let { GameManager.instance?.addInputProcessor(it) }
    }

    override fun hide() {
        super.hide()
        menuStage?.let { GameManager.instance?.removeInputProcessor(it) }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.camera.update()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)

        menuStage?.apply { act();draw() }
    }

    override fun dispose() {
        bgStage?.dispose()
        menuStage?.dispose()
        sceneLoader?.dispose()
        bgStage = null
        menuStage = null
        exitDialog = null
    }
}