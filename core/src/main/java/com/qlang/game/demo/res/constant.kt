package com.qlang.game.demo.res

object Direction {
    const val NONE = 0xff
    const val LEFT = 0x1
    const val RIGHT = 0x2
    const val UP = 0x4
    const val DOWN = 0x8
}

object Status {
    const val NONE = -1
    const val IDLE = 0
    const val ATTACK = 1
    const val ACTION = 2
    const val WALK = 3
    const val JUMP = 4
    const val RUN = 5
    const val HIT = 6
    const val PUNCH = 7
    const val CHOP = 8
    const val SLEEP = 9
    const val WAKEUP = 10
    const val PICKUP = 11
}

object Tool {
    const val axe = 0
    const val blowdart = 0
    const val boomerang = 0
    const val bugnet = 0
    const val fishing = 0
    const val shovel = 0
    const val unsaddle = 0
    const val whip = 0
}

object Weapon {

}