package com.qlang.game.demo.actor

object PlayerStatus {
    const val IDLE = 0
    const val ATTACK = 1
    const val ACTION = 2
    const val WALK = 3
    const val JUMP = 4
}

enum class PlayerBodyIndexState {
    FULL, FILL_UP, FILL_DOWN
}