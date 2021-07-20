package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Player
import com.qlang.game.demo.res.Status
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class PlayerScript : BasicScript {
    private val LEFT = Direction.LEFT
    private val RIGHT = Direction.RIGHT
    private val UP = Direction.UP
    private val DOWN = Direction.DOWN

    private var engine: PooledEngine? = null

    private var spriterComponent: SpriterObjectComponent? = null
    private var playerComponent: PlayerComponent? = null
    private var transformComponent: TransformComponent? = null

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)

        item?.let {
            ItemWrapper(it).getChild("role")?.entity?.let { role ->
                spriterComponent = ComponentRetriever.get(role, SpriterObjectComponent::class.java)
                playerComponent = ComponentRetriever.get(role, PlayerComponent::class.java)
            }
            transformComponent = ComponentRetriever.get(it, TransformComponent::class.java)

            spriterComponent?.animation?.startPlay()
        }
    }

    private fun movePlayer(direction: Int) {
        transformComponent?.apply {
            when (direction) {
                LEFT -> x -= 5f
                RIGHT -> x += 5f
                UP -> y += 5f
                DOWN -> y -= 5f
            }
        }

        playerComponent?.apply { this.status = Status.RUN;this.direction = direction }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> movePlayer(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(DOWN)
            Gdx.input.isKeyPressed(Input.Keys.SPACE) -> playerComponent?.status = Status.ACTION
            else -> playerComponent?.status = Status.IDLE
        }

        update()
    }

    private fun update() {
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

    override fun dispose() {
    }

}