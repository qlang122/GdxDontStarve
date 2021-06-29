package com.qlang.game.demo.component

import games.rednblack.editor.renderer.components.BaseComponent

class PlayerComponent : BaseComponent {
    var direction: Int = Player.Direction.DOWN
    var isRun = false

    override fun reset() {
    }
}