package com.qlang.game.demo.actor.player

import com.badlogic.gdx.Gdx
import com.qlang.game.demo.res.Player
import com.qlang.game.demo.res.R

class WilsonActor : PlayerActor(R.anim.player.wilson_atlas) {

    init {
        manager?.let {
            loadScml(it, R.anim.player.basic)
            loadScml(it, R.anim.player.idle)
            loadEntity(entityName)
            loadAnimation(Player.Anim.Idle.loop_down)
        }
        animation?.root?.position?.set(Gdx.graphics.width / 2f, Gdx.graphics.height / 2 - 50f)
        animation?.root?.setScale(0.6f)
        animation?.makeTimelineVisible(HashMap<String, Boolean>().apply {
            put("hand_2", false);put("arm_upper_2", false);put("arm_lower_2", false)
        })
    }

}