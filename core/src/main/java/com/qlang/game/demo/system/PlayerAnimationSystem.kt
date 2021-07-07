package com.qlang.game.demo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.component.Player
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerAnimationSystem : IteratingSystem(Family.all(PlayerComponent::class.java).get()) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val playerComponent = ComponentRetriever.get(entity, PlayerComponent::class.java)
        val spriterComponent = ComponentRetriever.get(entity, SpriterObjectComponent::class.java)
        val transformComponent = ComponentRetriever.get(entity, TransformComponent::class.java)

        spriterComponent?.setAnimation(entity, when (playerComponent?.direction) {
            Player.Direction.LEFT -> {
                transformComponent?.flipX = true
                if (playerComponent.isRun) Player.Anim.Run.loop_side else Player.Anim.Idle.loop_side
            }
            Player.Direction.RIGHT -> {
                transformComponent?.flipX = false
                if (playerComponent.isRun) Player.Anim.Run.loop_side else Player.Anim.Idle.loop_side
            }
            Player.Direction.UP -> {
                if (playerComponent.isRun) Player.Anim.Run.loop_up else Player.Anim.Idle.loop_up
            }
            Player.Direction.DOWN -> {
                if (playerComponent.isRun) Player.Anim.Run.loop_down else Player.Anim.Idle.loop_down
            }
            else -> {
                Player.Anim.Idle.loop_down
            }
        })
        spriterComponent?.play()
    }
}