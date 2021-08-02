package com.qlang.game.demo.component

import com.badlogic.ashley.core.Entity
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.entity.PlayerInfo
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import games.rednblack.editor.renderer.components.BaseComponent

typealias OnAnimChangeListener = (anim: String?) -> Unit

class PlayerComponent : BaseComponent {
    var body: PlayerInfo.Body = PlayerInfo.Body()

    var direction: Int = Direction.DOWN
    var status: Int = Status.IDLE
    var subStatus = Status.NONE
    var isAutoRun = false
    var handItemType: Int = GoodsInfo.Type.UNKNOW.value

    var goalEntity: Entity? = null
    var goalType: Int = GoodsInfo.Type.UNKNOW.value

    val onAnimationChangeListeners: ArrayList<OnAnimChangeListener> = ArrayList()

    constructor()

    fun addAnimChangeListener(lis: (anim: String?) -> Unit) {
        onAnimationChangeListeners.add(lis)
    }

    fun removeAnimChangeListener(lis: (anim: String?) -> Unit) {
        onAnimationChangeListeners.remove(lis)
    }

    override fun reset() {
        direction = Direction.DOWN
        status = Status.IDLE
        subStatus = Status.NONE
        isAutoRun = false
        onAnimationChangeListeners.clear()
    }
}