package com.qlang.game.demo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.res.Player
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerAnimationSystem : IteratingSystem(Family.all(PlayerComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val playerComponent = ComponentRetriever.get(entity, PlayerComponent::class.java)
        val spriterComponent = ComponentRetriever.get(entity, SpriterObjectComponent::class.java)
        val transformComponent = ComponentRetriever.get(entity, TransformComponent::class.java)

        spriterComponent?.setAnimation(entity, getAnimName(playerComponent))
        when (playerComponent?.direction) {
            Direction.LEFT -> transformComponent?.flipX = true
            Direction.RIGHT -> transformComponent?.flipX = false
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