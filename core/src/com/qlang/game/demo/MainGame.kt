package com.qlang.game.demo

import com.badlogic.gdx.Game
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.screen.LaunchScreen

class MainGame() : Game() {

    private val assetManager = GameAssetManager.instance

    init {
        Navigator.instance?.init(this)
    }

    override fun create() {
        Navigator.push(LaunchScreen())
        assetManager?.create()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        assetManager?.resize(width, height)
    }

    override fun pause() {
        super.pause()
        assetManager?.pause()
    }

    override fun resume() {
        super.resume()
        assetManager?.resume()
    }

    override fun dispose() {
        super.dispose()
        Navigator.instance?.dispose()
        assetManager?.dispose()
    }
}