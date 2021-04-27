package com.qlang.game.demo.entity

data class WorlInfo(var id: Int, var role: String?, var name: String?, var days: Int, var time: Long?, var createTime: Long?) {
    constructor() : this(0, null, null, 0, 0L, 0L)
}