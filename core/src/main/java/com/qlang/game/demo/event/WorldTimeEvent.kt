package com.qlang.game.demo.event

import com.qlang.game.demo.entity.GameDate

object WorldTimeEvent {
    var date: GameDate? = null

    override fun toString(): String {
        return "WorldTimeEvent(date=$date)"
    }
}