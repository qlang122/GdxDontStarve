package com.qlang.game.demo.component

import com.badlogic.ashley.core.Entity
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.entity.PlayerInfo
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import games.rednblack.editor.renderer.components.BaseComponent

class PlayerComponent : BaseComponent {
    var body: PlayerInfo.Body = PlayerInfo.Body()

    var direction: Int = Direction.DOWN
    var status: Int = Status.IDLE
    var subStatus = Status.NONE
    var isAutoRun = false
    var handItemType: Int = GoodsInfo.Type.UNKNOW.value

    var goalEntity: Entity? = null
    var goalType: Int = GoodsInfo.Type.UNKNOW.value

    constructor()

    override fun reset() {
        direction = Direction.DOWN
        status = Status.IDLE
        subStatus = Status.NONE
        isAutoRun = false
    }
}