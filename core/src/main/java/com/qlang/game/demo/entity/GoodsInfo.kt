package com.qlang.game.demo.entity

class GoodsInfo : BaseInfo() {
    var type: Int = 0 and Type.UNKNOW.ordinal
    var subType: Int = 0 and Type.UNKNOW.ordinal
    var name: String = ""
    var desc: String = ""
    var num: Int = 0
    var effect: Int? = null
    var health: Float = 1.0f
    var enmity: Int = Enmity.MIDDLE.ordinal

    companion object {
        enum class Type(value: Byte) {
            UNKNOW(0x0), FOOD(0x01), TOOL(0x02), WEAPON(0x04),
            MATERIAL(0x08), ANIMAL(0x10), PLANT(0x20), BUILDING(0x40)
        }

        enum class Enmity() {
            MIDDLE, YES, NO
        }
    }

    fun reset() {
        type = 0 and Type.UNKNOW.ordinal
        subType = 0 and Type.UNKNOW.ordinal
        name = ""
        desc = ""
        num = 0
        effect = null
        health = 1.0f
    }
}