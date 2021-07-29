package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class BerryScript : EntityScript {
    private var spriterComponent: SpriterObjectComponent? = null

    constructor(engine: PooledEngine) : super(engine) {

    }

    override fun init(item: Entity?) {
        super.init(item)
        item?.let {
            spriterComponent = ItemWrapper(it).getChild("role")?.entity?.let {
                ComponentRetriever.get(it, SpriterObjectComponent::class.java)
            }
            spriterComponent?.animation?.startPlay()
        }
    }

    override fun act(delta: Float) {
    }

    override fun dispose() {
    }

}