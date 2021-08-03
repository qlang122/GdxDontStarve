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
import com.qlang.game.demo.utils.Log
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import me.winter.gdx.animation.AnimatorListener
import me.winter.gdx.animation.MainlineKey

class PlayerSystem : IteratingSystem(Family.all(PlayerComponent::class.java).get()) {
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val playerMapper: ComponentMapper<PlayerComponent> = ComponentMapper.getFor(PlayerComponent::class.java)
    private val spriterMapper: ComponentMapper<SpriterObjectComponent> = ComponentMapper.getFor(SpriterObjectComponent::class.java)

    private var oldAnim: String? = null
    private var oldStatus: Int? = null
    private var oldDirection: Int? = null

    private var playerComponent: PlayerComponent? = null
    private var spriterComponent: SpriterObjectComponent? = null

    private var canUpdateAnim = true
    private var canLoop = true
    private val onAnimatorListener: AnimatorListener = object : AnimatorListener {
        override fun onProgress(index: Int, total: Int) {
        }

        override fun onEnd(key: MainlineKey?, index: Int) {
            canUpdateAnim = true
        }

        override fun onStart(key: MainlineKey?, index: Int) {
        }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        playerComponent = playerComponent ?: playerMapper.get(entity)
        spriterComponent = spriterComponent ?: spriterMapper.get(entity)
        val transformComponent = transformMapper.get(entity)

        checkStatus(playerComponent, entity)

        when (playerComponent?.direction) {
            Direction.LEFT, (Direction.LEFT + Direction.UP),
            (Direction.LEFT + Direction.DOWN) -> transformComponent?.flipX = true
            else -> transformComponent?.flipX = false
        }
    }

    private fun setAnimation(entity: Entity?, anim: String, loop: Boolean = true) {
        if (anim.isEmpty()) return

        spriterComponent?.run { setAnimation(entity, anim);isLooping = loop;play() }

        if (oldAnim != anim) {
            spriterComponent?.animation?.setAnimatorListener(onAnimatorListener)
            playerComponent?.onAnimationChangeListeners?.forEach { it(anim) }
        }
        oldAnim = anim
    }

    private fun checkStatus(component: PlayerComponent?, entity: Entity?) {
        if (!canUpdateAnim) return
        if (oldDirection == component?.direction && oldStatus == component?.status) return

        oldDirection = component?.direction
        oldStatus = component?.status

        val tool = GoodsInfo.Type.TOOL.value
        val weapon = GoodsInfo.Type.WEAPON.value

        var anim = ""
        canLoop = true
        Log.e("QL", "------>", component?.direction, component?.status, component?.goalType, component?.handItemType)
        when (component?.direction) {
            Direction.LEFT, Direction.RIGHT, (Direction.LEFT + Direction.UP),
            (Direction.LEFT + Direction.DOWN), (Direction.RIGHT + Direction.UP),
            (Direction.RIGHT + Direction.DOWN) -> {
                anim = Player.Anim.Idle.loop_side
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> anim = Player.Anim.Run.loop_side
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    anim = Player.Anim.Attack.atk_side
                                    canLoop = false
                                }
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    anim = Player.Anim.Action_axe.chop_loop_side
                                }
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Attack.atk_side
                                }
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Action_pickaxe.pickaxe_loop_side
                                }
                            }
                        }
                    }
                }
            }
            Direction.UP -> {
                anim = Player.Anim.Idle.loop_up
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> anim = Player.Anim.Run.loop_up
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    anim = Player.Anim.Attack.atk_up
                                }
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    anim = Player.Anim.Action_axe.chop_loop_up
                                }
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Attack.atk_up
                                }
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Action_pickaxe.pickaxe_loop_up
                                }
                            }
                        }
                    }
                }
            }
            Direction.DOWN -> {
                anim = Player.Anim.Idle.loop_down
                val handItemType = component.handItemType
                when (component.status) {
                    Status.RUN -> anim = Player.Anim.Run.loop_down
                    Status.ATTACK -> {
                        when (component.goalType) {
                            GoodsInfo.Type.ANIMAL.value -> {
                                if ((handItemType and tool) == tool || (handItemType and weapon) == weapon) {
                                    anim = Player.Anim.Attack.atk_down
                                }
                            }
                            GoodsInfo.Type.PLANT.value -> {
                                if (handItemType and tool == tool) {
                                    anim = Player.Anim.Action_axe.chop_loop_down
                                }
                            }
                            GoodsInfo.Type.BUILDING.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Attack.atk_down
                                }
                            }
                            GoodsInfo.Type.MINERAL.value -> {
                                if ((handItemType and tool) == tool) {
                                    anim = Player.Anim.Action_pickaxe.pickaxe_loop_down
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!canLoop) canUpdateAnim = false

        setAnimation(entity, anim, canLoop)

    }
}