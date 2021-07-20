package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

open class EntitySystem : IteratingSystem(Family.all(EntityComponent::class.java).get()) {
    private val mainItemMapper: ComponentMapper<MainItemComponent> = ComponentMapper.getFor(MainItemComponent::class.java)
    private val entityMapper: ComponentMapper<EntityComponent> = ComponentMapper.getFor(EntityComponent::class.java)
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val dimensionsMapper: ComponentMapper<DimensionsComponent> = ComponentMapper.getFor(DimensionsComponent::class.java)

    private var playerComponent: PlayerComponent? = null

    private var player: Entity? = null

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (player == null || playerComponent == null) return

        val m = mainItemMapper.get(entity)
        if (!m.culled) {
            val t = transformMapper.get(entity)
            val d = dimensionsMapper.get(entity)
            val ec = entityMapper.get(entity)

            val pT = transformMapper.get(player)
            val pD = dimensionsMapper.get(player)

            if (abs(sqrt(pT.x.minus(t.x + 0.0).pow(2.0) + pT.y.minus(t.y + 0.0).pow(2.0))) < 150) {

            }
        }
    }

    fun setPlayer(player: Entity?) {
        this.player = player
        player?.let { playerComponent = ComponentRetriever.get(it, PlayerComponent::class.java) }
    }
}