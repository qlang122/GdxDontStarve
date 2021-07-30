package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R
import com.qlang.game.demo.route.Navigator

class PlayLoadingScreen : ScreenAdapter() {
    private var stage: Stage? = null
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private lateinit var txtLabel: Label
    private var delay: Float = 0f
    private val TXT_LOAD = "正在加载."

    init {
        manager?.let { mgr ->
            stage = Stage()

            val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)
//            stage?.addActor(Image(TextureRegionDrawable(mgr.get(R.image.bg_spiral_anim, TextureAtlas::class.java).findRegion("spiral_bg"))).apply {
//                setSize(Gdx.graphics.width.plus(0f), Gdx.graphics.height.plus(0f))
//            })

            txtLabel = Label("正在加载.", hudSkin, "font24").apply {
                setPosition(120f, 80f)
            }
            stage?.addActor(txtLabel)
        }
    }

    override fun show() {
        delay = 0f
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        delay += delta

        GameManager.instance?.playManager?.update()

        when (delay.toInt() % 3) {
            1 -> txtLabel.setText("${TXT_LOAD}.")
            2 -> txtLabel.setText("${TXT_LOAD}..")
            else -> txtLabel.setText("$TXT_LOAD")
        }

        if (delay >= 2 && GameManager.instance?.isPlayAssetsLoaded == true) {
            Navigator.push(PlayScreen())
            Navigator.pop(this)
            return
        }
        stage?.apply { act();draw() }
    }

    override fun dispose() {
        stage?.dispose()
        stage = null
    }
}