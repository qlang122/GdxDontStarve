package com.qlang.game.demo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.qlang.game.demo.res.R
import kotlin.random.Random

class GameManager private constructor() : ApplicationAdapter() {
    lateinit var mainManager: AssetManager
        private set
    lateinit var playManager: AssetManager
        private set
    lateinit var game: Game
        private set

    private val processors = ArrayList<InputProcessor>()

    var homeMenuBgIndex = Random.nextInt(3)
        private set

    companion object {
        var instance: GameManager? = GameManager()

    }

    override fun create() {
        mainManager = AssetManager()
        playManager = AssetManager()

        mainManager.loadAssets()
        mainManager.finishLoading()

        Gdx.input.inputProcessor = MyInputProcessor()
    }

    private fun AssetManager.loadAssets() {
        load(R.font.font_cn, BitmapFont::class.java)
        R.image.atlas().forEach { load(it, TextureAtlas::class.java) }
        load(when (homeMenuBgIndex) {
            1 -> R.anim.menu.feast_atlas
            2 -> R.anim.menu.halloween_atlas
            3 -> R.anim.menu.lunacy_atlas
            else -> R.anim.menu.base_atlas
        }, TextureAtlas::class.java)
        if (homeMenuBgIndex == 1) {
            load(R.anim.menu.feast_bg_atlas, TextureAtlas::class.java)
        }
    }

    val mainAssetsLoadProgress: Float get() = mainManager.progress
    val isMainAssetsLoaded: Boolean get() = mainManager.isFinished

    fun AssetManager.loadPlayAssets() {
        load(R.anim.player.wilson.atlas, TextureAtlas::class.java)
        R.image.fxs.atlas().forEach { load(it, TextureAtlas::class.java) }
        R.image.tile.atlas().forEach { load(it, TextureAtlas::class.java) }
    }

    val playAssetsLoadProgress: Float get() = mainManager.progress
    val isPlayAssetsLoaded: Boolean get() = mainManager.isFinished

    fun <T : Game> init(game: T) {
        this.game = game
    }

    fun addInputProcessor(processor: InputProcessor) {
        processors.add(processor)
    }

    fun removeInputProcessor(processor: InputProcessor) {
        processors.remove(processor)
    }

    override fun dispose() {
        mainManager.dispose()
        playManager.dispose()

        instance = null
    }

    private inner class MyInputProcessor : InputProcessor {
        override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            processors.forEach { it.touchUp(screenX, screenY, pointer, button) }
            return true
        }

        override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
            processors.forEach { it.mouseMoved(screenX, screenY) }
            return true
        }

        override fun keyTyped(character: Char): Boolean {
            processors.forEach { it.keyTyped(character) }
            return true
        }

        override fun scrolled(amountX: Float, amountY: Float): Boolean {
            processors.forEach { it.scrolled(amountX, amountY) }
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            processors.forEach { it.keyUp(keycode) }
            return true
        }

        override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
            processors.forEach { it.touchDragged(screenX, screenY, pointer) }
            return true
        }

        override fun keyDown(keycode: Int): Boolean {
            processors.forEach { it.keyDown(keycode) }
            return true
        }

        override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
            processors.forEach { it.touchDown(screenX, screenY, pointer, button) }
            return true
        }

    }
}