package com.qlang.game.demo.entity

import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status

open class BaseInfo {
    var uId = -1
    var id = ""
    var x = 0f
    var y = 0f
    var scaleX = 1f
    var scaleY = 1f
    var rotation = 0f
    var zIndex = 0
    var direction: Int = Direction.NONE
    var status: Int = Status.IDLE

    constructor()
}