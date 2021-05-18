package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R

class PlayHudStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont11: BitmapFont? = null
    private var bitmapFont13: BitmapFont? = null

    init {
        manager?.let { mgr ->
            bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }?.let {
                bitmapFont11 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.1f) }
                bitmapFont13 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.3f) }
                it
            }
            val uiTexture = mgr.get(R.image.ui, TextureAtlas::class.java)


        }
    }

}