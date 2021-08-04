package com.qlang.game.demo.entity

class GoodsInfo : BaseInfo() {
    var type: Int = 0 + Type.ANIMAL.value
    var subType: Int = 0 + Type.UNKNOW.value
    var name: String = ""
    var desc: String = ""
    var num: Int = 0
    var effect: Int? = null
    var health: Float = 1.0f
    var enmity: Int = Enmity.MIDDLE.ordinal

    enum class Type(val value: Int) {
        UNKNOW(0x0), FOOD(0x01), TOOL(0x02), WEAPON(0x04),
        MATERIAL(0x08), ANIMAL(0x10), PLANT(0x20), BUILDING(0x40),
        MINERAL(0x80)
    }

    enum class Enmity {
        MIDDLE, YES, NO
    }

    fun reset() {
        type = 0 + Type.UNKNOW.value
        subType = 0 + Type.UNKNOW.value
        name = ""
        desc = ""
        num = 0
        effect = null
        health = 1.0f
    }
}