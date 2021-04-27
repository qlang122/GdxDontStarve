package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.qlang.game.demo.actor.main.MenuBgAnimActor
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R

class HomeBgStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    init {
        val screenWidth: Float = Gdx.graphics.width.plus(0f)
        val screenHeight: Float = Gdx.graphics.height.plus(0f)

        manager?.let {

            for (actor in MenuBgAnimActor().createActor()) {
                addActor(actor)
            }
            it.trycatch {
                get(R.image.frontscreen, TextureAtlas::class.java)
            }?.findRegion("title")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    align = Align.left
                    val h = Gdx.graphics.height - screenHeight / 2 + 80
                    setPosition(80f, h)
                    height = screenHeight / 2 - 80;setScaling(Scaling.fillY)
                })
            }

            it.trycatch {
                get(R.image.bg_redux_bottom_solid, TextureAtlas::class.java)
            }?.findRegion("dark_bottom_solid")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    align = Align.bottom;
                    setSize(screenWidth, screenHeight / 2 + 50)
                })
            }
            it.trycatch {
                get(R.image.bg_redux_bottom, TextureAtlas::class.java)
            }?.findRegion("dark_bottom")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    align = Align.bottom;
                    setSize(screenWidth, screenHeight / 2 + 50)
                })
            }
//            it.trycatch {
//                get(R.image.bg_redux_bottom_over, TextureAtlas::class.java)
//            }?.findRegion("dark_bottom_vignette1")?.let {
//                addActor(Image(TextureRegion(it)).apply {
//                    align = Align.bottom;
//                    setSize(screenWidth, screenHeight / 2 + 50)
//                })
//            }
        }
    }
}