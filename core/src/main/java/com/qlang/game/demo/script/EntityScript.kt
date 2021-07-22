package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.qlang.game.demo.component.EntityComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

abstract class EntityScript : BasicScript {
    protected var engine: PooledEngine? = null
    protected var entityComponent: EntityComponent? = null

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)
        entityComponent = item?.let { ComponentRetriever.get(it, EntityComponent::class.java) }
    }
}