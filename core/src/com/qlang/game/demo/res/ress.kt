package com.qlang.game.demo.res

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas

class GameAssetManager private constructor() : ApplicationAdapter() {
    lateinit var manager: AssetManager
        private set

    companion object {
        var instance: GameAssetManager? = GameAssetManager()

    }

    override fun create() {
        manager = AssetManager()
        manager.loadAssets()
        manager.finishLoading()
    }

    private fun AssetManager.loadAssets() {
        load(R.anim.Wilson.atlas, TextureAtlas::class.java)
    }

    fun getLoadProgress(): Float = manager.progress
    fun isLoadFinish(): Boolean = manager.isFinished

    override fun dispose() {
        manager.apply { clear();dispose() }

        instance = null
    }
}

object R {
    object anim {
        object Wilson {
            const val atlas = "anim/wilson/wilson.atlas"
            const val scml_idle = "anim/wilson/wilson-idles.scml"
            const val scml_basic = "anim/wilson/wilson-basic.scml"
        }
    }
}