package com.qlang.game.demo.entity

data class HudTabGoodsAttr(var icon: String, var goods: GoodsInfo?, var index: Int) {
    constructor() : this("", null, 0)
}