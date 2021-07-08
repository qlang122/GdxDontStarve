package com.qlang.game.demo.entity

class PlayerInfo : BaseInfo() {
    var body: Body = Body()
    var temperature = 26

    class Body {
        var HEALTH: Int = 200
        var HUNGER: Int = 150
        var SANTITY: Int = 150
        var WET: Int = 0

        var currHealth: Int = HEALTH
        var currHunger: Int = HUNGER
        var currSanity: Int = SANTITY
        var currWet: Int = WET

        constructor()
    }
}