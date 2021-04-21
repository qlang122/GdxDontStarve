package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.FillViewport
import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log

class HomeBgStage : Stage(FillViewport(Gdx.graphics.width.plus(0f), Gdx.graphics.height.plus(0f) / 2 - 50)) {
    private val manager: AssetManager? = GameAssetManager.instance?.mainManager

    init {
        val screenWidth: Float = Gdx.graphics.width.plus(0f)
        val screenHeight: Float = Gdx.graphics.height.plus(0f)

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
                    align = Align.bottom;setScaling(Scaling.fillY)
                    setSize(screenWidth, screenHeight / 2 - 50)
                })
            }
            it.trycatch {
                get(R.image.bg_redux_bottom, TextureAtlas::class.java)
            }?.findRegion("dark_bottom")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    align = Align.bottom;setScaling(Scaling.fillY)
                    setSize(screenWidth, screenHeight / 2 - 50)
                })
            }
            it.trycatch {
                get(R.image.bg_redux_bottom_over, TextureAtlas::class.java)
            }?.findRegion("dark_bottom_vignette1")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    align = Align.bottom;setScaling(Scaling.fillY)
                    setSize(screenWidth, screenHeight / 2 - 50)
                })
            }
        }
    }
}