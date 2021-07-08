package com.qlang.game.demo.event

object PlayerBodyIndexEvent {
    var health: Float = 0f
    var hunger: Float = 0f
    var sanity: Float = 0f
    var wet: Float = 0f

    override fun toString(): String {
        return "PlayerBodyIndexEvent(health=$health, hunger=$hunger, sanity=$sanity, wet=$wet)"
    }
}