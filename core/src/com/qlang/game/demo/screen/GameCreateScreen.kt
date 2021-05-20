package com.qlang.game.demo.screen

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.stage.GameCreateStage

class GameCreateScreen : ScreenAdapter() {
    private var createStage: Stage? = null

    init {
        createStage = GameCreateStage()

    }

    override fun render(delta: Float) {
        createStage?.apply { act();draw() }
    }

    override fun dispose() {
        createStage?.dispose()
    }
}