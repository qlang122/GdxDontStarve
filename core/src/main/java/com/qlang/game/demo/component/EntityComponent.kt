package com.qlang.game.demo.component

import com.qlang.game.demo.entity.GoodsInfo
import games.rednblack.editor.renderer.components.BaseComponent

class EntityComponent : BaseComponent {
    val info: GoodsInfo = GoodsInfo()

    override fun reset() {
        info.reset()
    }
}