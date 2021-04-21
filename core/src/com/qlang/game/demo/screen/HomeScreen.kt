package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.actor.player.WilsonActor
import com.qlang.game.demo.stage.HomeBgStage

class HomeScreen : ScreenAdapter() {
    private var stage: Stage? = null
    private var bgStage: Stage? = null

    init {
        stage = Stage()
        bgStage = HomeBgStage()

//        stage?.addActor(WilsonActor())
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage?.apply { act();draw() }
        bgStage?.apply { act();draw() }
    }

    override fun dispose() {
        stage?.dispose()
        bgStage?.dispose()
        stage = null
        bgStage = null
    }
}