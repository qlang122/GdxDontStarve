package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

class DragonflyScript : BasicScript {
    private var engine: PooledEngine? = null

    private var spriterComponent: SpriterObjectComponent? = null

    constructor(engine: PooledEngine) {
        this.engine = engine

    }

    override fun init(item: Entity?) {
        super.init(item)
        item?.let {
            spriterComponent = ComponentRetriever.get(it, SpriterObjectComponent::class.java)
            spriterComponent?.animation?.startPlay()
        }
    }

    override fun act(delta: Float) {
    }

    override fun dispose() {
    }
}