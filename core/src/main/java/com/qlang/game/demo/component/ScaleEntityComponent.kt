package com.qlang.game.demo.component

import games.rednblack.editor.renderer.components.BaseComponent

class ScaleEntityComponent : BaseComponent {
    var oldScaleX = -1f
    var oldScaleY = -1f

    override fun reset() {
        oldScaleX = -1f
        oldScaleY = -1f
    }
}