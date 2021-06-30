package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
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
    private var transformComponent: TransformComponent? = null

    private val impulse: Vector2 = Vector2(0f, 0f)
    private val speed: Vector2 = Vector2(0f, 0f)

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)

        item?.let {
            physicsComponent = ComponentRetriever.get(it, PhysicsBodyComponent::class.java)
            spriterComponent = ComponentRetriever.get(it, SpriterObjectComponent::class.java)
            transformComponent = ComponentRetriever.get(it, TransformComponent::class.java)
            spriterComponent?.animation?.startPlay()
        }
    }

    private fun movePlayer(direction: Int) {
//        val body = physicsComponent?.body
//        body?.linearVelocity?.let { speed.set(it) }
//
//        when (direction) {
//            LEFT -> impulse.set(-5f, speed.y)
//            RIGHT -> impulse.set(5f, speed.y)
//            UP -> impulse.set(speed.x, 5f)
//            DOWN -> impulse.set(speed.x, -5f)
//        }
//        val sub = impulse.sub(speed)
//        Log.e("QL", "--->", body?.linearVelocity, speed, impulse, sub, body?.worldCenter)
//        body?.applyLinearImpulse(sub, body.worldCenter, true)

        transformComponent?.apply {
            Log.e("QL", "----->>", x, y)
            when (direction) {
                LEFT -> x += 5f
                RIGHT -> x -= 5f
                UP -> y += 5f
                DOWN -> y -= 5f
            }
        }

        getPlayerComponent()?.apply { this.isRun = true;this.direction = direction }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) -> movePlayer(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(DOWN)
            else -> getPlayerComponent()?.isRun = false
        }
    }

    fun getPlayerComponent(): PlayerComponent? {
        return ComponentRetriever.get(entity, PlayerComponent::class.java)
    }

    override fun dispose() {
    }

}