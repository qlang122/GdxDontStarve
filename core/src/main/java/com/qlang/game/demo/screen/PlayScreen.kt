package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.stage.PlayHudStage
import com.qlang.game.demo.stage.PlayStage
import com.qlang.game.demo.stage.TestStage

class PlayScreen : ScreenAdapter() {
    private var hudStage: PlayHudStage? = null
    private var playStage: PlayStage? = null

    init {
        hudStage = PlayHudStage()
        playStage = PlayStage()

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        playStage?.apply { act();draw() }
//        hudStage?.apply { act();draw() }
    }

    override fun show() {
        super.show()
        hudStage?.let { GameManager.instance?.addInputProcessor(it) }
    }

    override fun hide() {
        super.hide()
        hudStage?.let { GameManager.instance?.removeInputProcessor(it) }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        playStage?.viewport?.update(width, height)
        playStage?.resize(width, height)
        hudStage?.viewport?.update(width, height)
        hudStage?.resize(width, height)
    }

    override fun dispose() {
        playStage?.dispose()
        hudStage?.dispose()
        playStage = null
        hudStage = null
    }
}