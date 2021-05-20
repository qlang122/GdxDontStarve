package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.GameManager

class GameCreateStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    init {
        manager?.let { mgr ->

        }
    }
}