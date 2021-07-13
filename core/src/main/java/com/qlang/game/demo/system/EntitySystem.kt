package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever

class EntitySystem : IteratingSystem(Family.all(EntityComponent::class.java).get()) {
    private val mainItemMapper: ComponentMapper<MainItemComponent> = ComponentMapper.getFor(MainItemComponent::class.java)
    private val entityMapper: ComponentMapper<EntityComponent> = ComponentMapper.getFor(EntityComponent::class.java)

    private var playerComponent: PlayerComponent? = null

    private var player: Entity? = null

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (player == null || playerComponent == null) return


    }

    fun setPlayer(player: Entity?) {
        this.player = player
        player?.let { playerComponent = ComponentRetriever.get(it, PlayerComponent::class.java) }
    }
}