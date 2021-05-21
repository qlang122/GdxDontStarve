package com.qlang.game.demo.entity

class GoodsInfo {
    var type: Int = 0 and Type.UNKNOW.ordinal
    var name: String? = null
    var desc: String? = null
    var num: Int = 0
    var effect: Int? = null

    companion object {
        enum class Type(value: Byte) {
            UNKNOW(0x0), FOOD(0x01), TOOL(0x02), WEAPON(0x04),
            MATERIAL(0x08), ANIMAL(0x10)
        }
    }
}