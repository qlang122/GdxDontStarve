package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R

class PlayMapStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    init {
        manager?.let { mgr ->
            val hudTexture = mgr.get(R.image.hud, TextureAtlas::class.java)


        }
    }
}