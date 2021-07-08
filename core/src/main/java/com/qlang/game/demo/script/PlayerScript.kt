package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerScript : BasicScript {
    private val LEFT = Direction.LEFT
    private val RIGHT = Direction.RIGHT
    private val UP = Direction.UP
    private val DOWN = Direction.DOWN

    private var engine: PooledEngine? = null

    private var physicsComponent: PhysicsBodyComponent? = null
    private var spriterComponent: SpriterObjectComponent? = null
    private var playerComponent: PlayerComponent? = null
    private var transformComponent: TransformComponent? = null

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)

        item?.let {
            physicsComponent = ComponentRetriever.get(it, PhysicsBodyComponent::class.java)
            spriterComponent = ComponentRetriever.get(it, SpriterObjectComponent::class.java)
            playerComponent = ComponentRetriever.get(it, PlayerComponent::class.java)
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
    }

    override fun dispose() {
    }

}