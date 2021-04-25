package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R

class ExitAppStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var onOkListener: (() -> Unit)? = null
    private var onCancelListener: (() -> Unit)? = null

    init {
        manager?.let { mgr ->
            Gdx.input.inputProcessor = this

            mgr.trycatch {
                get(R.image.globalpanels, TextureAtlas::class.java)
            }?.findRegion("small_dialog")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    setSize(920f, 640f)
                    setPosition(this@ExitAppStage.width / 2 - width / 2, this@ExitAppStage.height / 2 - height / 2)
                })
            }
            val bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }?.let {
                BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false), it.regions, true).apply {
                    data?.setScale(1.8f)
                }
            }

            mgr.trycatch {
                get(R.image.ui, TextureAtlas::class.java)
            }?.let {
                val style = TextButton.TextButtonStyle()
                style.up = TextureRegionDrawable(it.findRegion("button_long")).apply {
                    setPadding(20f, 40f, 20f, 40f)
                }
                style.down = TextureRegionDrawable(it.findRegion("button_long_over")).apply {
                    setPadding(20f, 40f, 20f, 40f)
                }
                style.font = bitmapFont
                addActor(TextButton("是", style).apply {
                    setPosition(this@ExitAppStage.width / 2 - width - 80f, this@ExitAppStage.height / 2 - 180f)
                    addListener { onOkListener?.invoke();true }
                })
            }
            mgr.trycatch {
                get(R.image.ui, TextureAtlas::class.java)
            }?.let {
                val style = TextButton.TextButtonStyle()
                style.up = TextureRegionDrawable(it.findRegion("button_long")).apply {
                    setPadding(20f, 40f, 20f, 40f)
                }
                style.down = TextureRegionDrawable(it.findRegion("button_long_over")).apply {
                    setPadding(20f, 40f, 20f, 40f)
                }
                style.font = bitmapFont
                addActor(TextButton("否", style).apply {
                    setPosition(this@ExitAppStage.width / 2 + 80f, this@ExitAppStage.height / 2 - 180f)
                    addListener { onCancelListener?.invoke();true }
                })
            }
        }
    }

    fun setOnClickListener(onOk: () -> Unit, onCancel: () -> Unit) {
        onOkListener = onOk
        onCancelListener = onCancel
    }
}