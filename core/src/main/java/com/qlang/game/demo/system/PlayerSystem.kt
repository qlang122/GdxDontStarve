package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.res.Player
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.entity.GoodsInfo
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
            Direction.LEFT, (Direction.LEFT + Direction.UP),
            (Direction.LEFT + Direction.DOWN) -> transformComponent?.flipX = true
            else -> transformComponent?.flipX = false
        }
        spriterComponent?.play()
    }

    private fun getAnimName(component: PlayerComponent?): String {
        val tool = GoodsInfo.Type.TOOL.value
        val weapon = GoodsInfo.Type.WEAPON.value

        return when (component?.direction) {
            Direction.LEFT, Direction.RIGHT, (Direction.LEFT + Direction.UP),
            (Direction.LEFT + Direction.DOWN), (Direction.RIGHT + Direction.UP),
            (Direction.RIGHT + Direction.DOWN) -> {
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_side
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    Player.Anim.Attack.atk_side
                                } else Player.Anim.Idle.loop_side
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    Player.Anim.Action_axe.chop_loop_side
                                } else Player.Anim.Idle.loop_side
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Attack.atk_side
                                } else Player.Anim.Idle.loop_side
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Action_pickaxe.pickaxe_loop_side
                                } else Player.Anim.Idle.loop_side
                            }
                            else -> Player.Anim.Idle.loop_side
                        }
                    }
                    else -> Player.Anim.Idle.loop_side
                }
            }
            Direction.UP -> {
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_up
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    Player.Anim.Attack.atk_up
                                } else Player.Anim.Idle.loop_up
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    Player.Anim.Action_axe.chop_loop_up
                                } else Player.Anim.Idle.loop_up
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Attack.atk_up
                                } else Player.Anim.Idle.loop_up
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Action_pickaxe.pickaxe_loop_up
                                } else Player.Anim.Idle.loop_up
                            }
                            else -> Player.Anim.Idle.loop_up
                        }
                    }
                    else -> Player.Anim.Idle.loop_up
                }
            }
            Direction.DOWN -> {
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> Player.Anim.Run.loop_down
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    Player.Anim.Attack.atk_down
                                } else Player.Anim.Idle.loop_down
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    Player.Anim.Action_axe.chop_loop_down
                                } else Player.Anim.Idle.loop_down
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Attack.atk_down
                                } else Player.Anim.Idle.loop_down
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    Player.Anim.Action_pickaxe.pickaxe_loop_down
                                } else Player.Anim.Idle.loop_down
                            }
                            else -> Player.Anim.Idle.loop_down
                        }
                    }
                    else -> Player.Anim.Idle.loop_down
                }
            }
            else -> {
                Player.Anim.Idle.loop_down
            }
        }
    }
}