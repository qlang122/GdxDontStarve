package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.res.Player
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent

class PlayerSystem : IteratingSystem(Family.all(PlayerComponent::class.java).get()) {
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val playerMapper: ComponentMapper<PlayerComponent> = ComponentMapper.getFor(PlayerComponent::class.java)
    private val spriterMapper: ComponentMapper<SpriterObjectComponent> = ComponentMapper.getFor(SpriterObjectComponent::class.java)

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val playerComponent = playerMapper.get(entity)
        val spriterComponent = spriterMapper.get(entity)
        val transformComponent = transformMapper.get(entity)

        spriterComponent?.setAnimation(entity, getAnimName(playerComponent))
        when (playerComponent?.direction) {
            Direction.LEFT -> transformComponent?.flipX = true
            else -> transformComponent?.flipX = false
        }
        spriterComponent?.play()
    }

    private fun getAnimName(component: PlayerComponent?): String {
        return when (component?.direction) {
            Direction.LEFT -> {
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_side
                    else -> Player.Anim.Idle.loop_side
                }
            }
            Direction.RIGHT -> {
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_side
                    else -> Player.Anim.Idle.loop_side
                }
            }
            Direction.UP -> {
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_up
                    else -> Player.Anim.Idle.loop_up
                }
            }
            Direction.DOWN -> {
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_down
                    else -> Player.Anim.Idle.loop_down
                }
            }
            else -> {
                Player.Anim.Idle.loop_down
            }
        }
    }
}