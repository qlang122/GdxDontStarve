package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.qlang.game.demo.component.EntityComponent
import games.rednblack.editor.renderer.components.PolygonComponent
import games.rednblack.editor.renderer.components.TransformComponent
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
        item?.let {
            entityComponent = ComponentRetriever.get(it, EntityComponent::class.java)
            val t = ComponentRetriever.get(it, TransformComponent::class.java)
            val p = ComponentRetriever.get(it, PolygonComponent::class.java)
            entityComponent?.makePolygon(p, t)
        }
    }
}