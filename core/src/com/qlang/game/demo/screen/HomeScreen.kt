package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.actor.WilsonActor
import com.qlang.game.demo.utils.Log

class HomeScreen : ScreenAdapter() {
    private var stage: Stage? = null

    init {
        stage = Stage()

        stage?.addActor(WilsonActor())
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage?.apply { act();draw() }
    }

    override fun dispose() {
        stage?.dispose()
        stage = null
    }
}