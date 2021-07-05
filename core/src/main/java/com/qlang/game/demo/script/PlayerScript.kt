package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.qlang.game.demo.component.Player
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.utils.Log
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerScript : BasicScript {
    private val LEFT = Player.Direction.LEFT
    private val RIGHT = Player.Direction.RIGHT
    private val UP = Player.Direction.UP
    private val DOWN = Player.Direction.DOWN

    private var engine: PooledEngine? = null

    private var physicsComponent: PhysicsBodyComponent? = null
    private var spriterComponent: SpriterObjectComponent? = null

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)

        item?.let {
            physicsComponent = ComponentRetriever.get(it, PhysicsBodyComponent::class.java)
            spriterComponent = ComponentRetriever.get(it, SpriterObjectComponent::class.java)
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

        playerComponent?.apply { this.isRun = true;this.direction = direction }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> movePlayer(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(DOWN)
            else -> playerComponent?.isRun = false
        }
    }

    val playerComponent: PlayerComponent? get() = ComponentRetriever.get(entity, PlayerComponent::class.java)

    val transformComponent: TransformComponent? get() = ComponentRetriever.get(entity, TransformComponent::class.java)

    override fun dispose() {
    }

}