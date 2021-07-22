package com.qlang.game.demo.component

import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.res.Status
import games.rednblack.editor.renderer.components.BaseComponent

class EntityComponent : BaseComponent {
    val info: GoodsInfo = GoodsInfo()
    var state: Int = Status.IDLE

    var playerDistance: Float = Float.NEGATIVE_INFINITY
    var isOverlapPlayer: Boolean = false

    var isBeAttack = false

    override fun reset() {
        info.reset()
    }
}