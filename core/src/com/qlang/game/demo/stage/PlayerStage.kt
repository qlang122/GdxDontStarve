package com.qlang.game.demo.stage

import com.badlogic.gdx.scenes.scene2d.Stage
import com.qlang.game.demo.actor.player.WilsonActor

class PlayerStage : Stage() {
    init {
        addActor(WilsonActor())
    }
}