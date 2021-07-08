package com.qlang.game.demo.component

import com.qlang.game.demo.entity.GameDate
import games.rednblack.editor.renderer.components.BaseComponent

class WorldComponent : BaseComponent {
    var time: Long = 0
    var gameDate = GameDate()

    override fun reset() {

    }
}