package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log

class ExitAppDialog {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var onOkListener: (() -> Unit)? = null
    private var onCancelListener: (() -> Unit)? = null

    private var dialog: Dialog? = null

    init {
        manager?.let { mgr ->
            val bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }?.let {
                BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false), it.regions, true).apply {
                    data?.setScale(1.5f)
                }
            }
            val bgRegion = mgr.trycatch {
                get(R.image.globalpanels, TextureAtlas::class.java)
            }?.findRegion("small_dialog")?.let {
                TextureRegionDrawable(it)
            }

            val w = Gdx.graphics.width * 3 / 7f
            dialog = Dialog("", Window.WindowStyle().apply {
                this.titleFont = bitmapFont;background = bgRegion
                background.minWidth = w
                background.minHeight = w * 2 / 3f
            })

            val btnOk = mgr.trycatch {
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
                TextButton("是", style).apply {
                    addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            onOkListener?.invoke()
                        }
                    })
                }
            }
            val btnCancel = mgr.trycatch {
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
                TextButton("否", style).apply {
                    addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            onCancelListener?.invoke()
                        }
                    })
                }
            }

            dialog?.contentTable?.add(Label("是否退出？", Label.LabelStyle(bitmapFont, null)))?.padTop(50f)
            dialog?.buttonTable?.let {
                it.add(btnOk).padRight(80f).padBottom(130f)
                it.add(btnCancel).padLeft(80f).padBottom(130f)
            }
        }
    }

    fun show(stage: Stage) {
        dialog?.show(stage)
    }

    fun hide() {
        dialog?.hide()
        dialog = null
    }

    fun cancel() {
        dialog?.cancel()
    }

    fun setOnClickListener(onOk: () -> Unit, onCancel: () -> Unit) {
        onOkListener = onOk
        onCancelListener = onCancel
    }
}