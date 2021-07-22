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
                LEFT + UP -> {
                    x -= 1.581f;y -= 1.581f//1.581=Math.sqrt(2.5);5=2(x*x)
                }
                LEFT + DOWN -> {
                    x -= 1.581f;y += 1.581f
                }
                RIGHT + UP -> {
                    x += 1.581f;y -= 1.581f
                }
                RIGHT + DOWN -> {
                    x += 1.581f;y += 1.581f
                }
            }
        }

        playerComponent?.apply {
            this.status = Status.RUN
            this.direction = direction
            this.isAutoRun = false
        }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) && !Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(DOWN)
            Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(LEFT + UP)
            Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(LEFT + DOWN)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(RIGHT + UP)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(RIGHT + DOWN)
            else -> playerComponent?.status = Status.IDLE
        }

        playerComponent?.subStatus = if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) Status.ACTION else Status.NONE

    }

    override fun dispose() {
    }

}