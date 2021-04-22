package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.route.Navigator


class LaunchScreen : ScreenAdapter() {
    private var bgTexture: TextureAtlas? = null
    private var bgOverlayTexture: TextureAtlas? = null
    private var bgImgTexture: TextureAtlas? = null

    private var bitmapFont: BitmapFont? = null

    private var screenStage: Stage? = null

    private var delay: Float = 0f

    private val NAEM_BG = "atlas/images/bg_spiral_anim.atlas"
    private val NAEM_BG_OVERLAY = "atlas/images/bg_spiral_anim_overlay.atlas"
    private val NAEM_BG_IMG = "atlas/images/bg_spiral_fill5.atlas"
    private val NAME_FONT = "fonts/font_load.fnt"

    private val TXT_LOAD = "正在加载."

    init {
        bgTexture = TextureAtlas(Gdx.files.internal(NAEM_BG))
        bgOverlayTexture = TextureAtlas(Gdx.files.internal(NAEM_BG_OVERLAY))
        bgImgTexture = TextureAtlas(Gdx.files.internal(NAEM_BG_IMG))

        bitmapFont = BitmapFont(Gdx.files.internal(NAME_FONT))
        bitmapFont?.data?.setScale(1.5f)

        screenStage = Stage()

        screenStage?.addActor(Image(bgTexture?.findRegion("spiral_bg")).apply {
            setFillParent(true)
        })
        screenStage?.addActor(Image(bgOverlayTexture?.findRegion("spiral_ol")).apply {
            setFillParent(true)
        })
        screenStage?.addActor(Image(bgImgTexture?.findRegion("bg_image5")).apply {
            setFillParent(true)
        })
    }

    override fun show() {
        delay = 0f
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        delay += delta

        if (delay >= 5f && GameAssetManager.instance?.isMainAssetsLoaded == true) {
            Navigator.push(HomeScreen())
            Navigator.pop(this)
            return
        }

        screenStage?.apply { act();draw() }
        screenStage?.batch?.let {
            it.begin()
            val t = delay.toInt() % 3
            bitmapFont?.draw(it, "${TXT_LOAD}${if (t == 1) "." else if (t == 2) ".." else ""}", 50f, 80f)
            it.end()
        }
    }

    override fun dispose() {
        screenStage?.dispose()
        bgTexture?.dispose()
        bgOverlayTexture?.dispose()
        bgImgTexture?.dispose()
        bitmapFont?.dispose()
    }
}