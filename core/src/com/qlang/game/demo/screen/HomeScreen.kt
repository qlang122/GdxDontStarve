package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.stage.ExitAppDialog
import com.qlang.game.demo.stage.HomeBgStage
import com.qlang.game.demo.stage.HomeMenuStage
import com.qlang.game.demo.utils.Log
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.resources.AsyncResourceManager

class HomeScreen : ScreenAdapter() {
    private var bgStage: Stage? = null
    private var menuStage: Stage? = null
    private var exitDialog: ExitAppDialog? = null

    private var sceneLoader: SceneLoader? = null
    private var viewport: ExtendViewport
    private val camera = OrthographicCamera()

    init {
        bgStage = HomeBgStage()
        menuStage = HomeMenuStage().apply {
            setOnItemClickListener { navigation2Menu(it) }
        }

        viewport = ExtendViewport(1920f, 1080f, 0f, 0f, camera)

        GameManager.instance?.mainManager?.let {
            sceneLoader = SceneLoader(it.get("project.dt", AsyncResourceManager::class.java))
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

        camera.update()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)

//        bgStage?.apply { act();draw() }
        menuStage?.apply { act();draw() }
    }

    override fun dispose() {
        bgStage?.dispose()
        menuStage?.dispose()
        bgStage = null
        menuStage = null
        exitDialog = null
    }
}