package com.qlang.game.demo

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
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
}