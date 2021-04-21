package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.res.R

class HomeBgStage : Stage() {
    private val manager: AssetManager? = GameAssetManager.instance?.mainManager

    init {
        val screenWidth: Float = Gdx.graphics.width.plus(0f)

        manager?.let {
            it.trycatch {
                get(R.image.frontscreen, TextureAtlas::class.java)
            }?.findRegion("title")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    val h = Gdx.graphics.height - imageHeight
                    setPosition(100f, if (h < 0) 0f else h)
                })
            }

            it.trycatch {
                get(R.image.bg_redux_bottom_solid, TextureAtlas::class.java)
            }?.findRegion("dark_bottom_solid")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    width = screenWidth
                })
            }
            it.trycatch {
                get(R.image.bg_redux_bottom, TextureAtlas::class.java)
            }?.findRegion("dark_bottom")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    width = screenWidth
                })
            }
            it.trycatch {
                get(R.image.bg_redux_bottom_over, TextureAtlas::class.java)
            }?.findRegion("dark_bottom_vignette1")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    width = screenWidth
                })
            }
        }
    }
}