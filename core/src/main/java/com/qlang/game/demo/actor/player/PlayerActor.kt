package com.qlang.game.demo.actor.player

import com.qlang.game.demo.actor.BaseAnimActor
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.Status

abstract class PlayerActor(textureAtlasName: String) : BaseAnimActor(textureAtlasName) {
    protected val manager = GameManager.instance?.playManager
    protected var status: Int = Status.IDLE

    protected var entityName = "wilson"
}