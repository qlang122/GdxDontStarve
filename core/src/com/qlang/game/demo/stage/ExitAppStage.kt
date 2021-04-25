package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.qlang.game.demo.config.trycatch
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.res.R

class ExitAppStage : Stage(ScalingViewport(Scaling.none, 320f, 190f)) {
    private val manager: AssetManager? = GameAssetManager.instance?.mainManager

    init {
        manager?.let { mgr ->
            mgr.trycatch {
                get(R.image.globalpanels, TextureAtlas::class.java)
            }?.findRegion("small_dialog")?.let {
                addActor(Image(TextureRegion(it)).apply {
                    setSize(this@ExitAppStage.width, this@ExitAppStage.height)
                })
            }
        }
    }
}