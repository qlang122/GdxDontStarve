package com.qlang.game.demo

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.screen.LaunchScreen

class MainGame() : Game() {

    private val gameManager = GameManager.instance

    init {
        Navigator.instance?.init(this)
        gameManager?.init(this)
    }

    override fun create() {
        Navigator.push(LaunchScreen())
        gameManager?.create()

        Gdx.input.setCatchKey(Input.Keys.BACK, true)
        Gdx.input.setCatchKey(Input.Keys.ESCAPE, true)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameManager?.resize(width, height)
    }

    override fun pause() {
        super.pause()
        gameManager?.pause()
    }

    override fun resume() {
        super.resume()
        gameManager?.resume()
    }

    override fun dispose() {
        super.dispose()
        Navigator.instance?.dispose()
        gameManager?.dispose()
    }
}