package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.stage.ExitAppStage
import com.qlang.game.demo.stage.HomeBgStage
import com.qlang.game.demo.stage.HomeMenuStage

class HomeScreen : ScreenAdapter() {
    private var bgStage: Stage? = null
    private var menuStage: Stage? = null
    private var exitStage: Stage? = null

    init {
        bgStage = HomeBgStage()
        menuStage = HomeMenuStage().apply {
            setOnItemClickListener { navigation2Menu(it) }
        }
    }

    private fun navigation2Menu(index: Int) {
        when (index) {
            0 -> {
            }
            1 -> {
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
            5 -> {
                exitStage = ExitAppStage().apply {
                    setOnClickListener({ Gdx.app.exit() }, { exitStage?.dispose();exitStage = null })
                }
            }
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        bgStage?.apply { act();draw() }
        menuStage?.apply { act();draw() }
        exitStage?.apply { act();draw() }
    }

    override fun dispose() {
        bgStage?.dispose()
        menuStage?.dispose()
        exitStage?.dispose()
        bgStage = null
        menuStage = null
        exitStage = null
    }
}