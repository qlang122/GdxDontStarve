package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.stage.PlayHudStage

class PlayScreen : ScreenAdapter() {
    private var hudStage: Stage? = null

    init {
        hudStage = PlayHudStage()

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        hudStage?.apply { act();draw() }
    }

    override fun dispose() {
        hudStage?.dispose()
        hudStage = null
    }
}