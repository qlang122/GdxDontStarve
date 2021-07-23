package com.qlang.game.demo.script

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import com.qlang.game.demo.tool.BidirectionalAStar
import com.qlang.game.demo.utils.Log
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashSet

class PlayerScript : BasicScript {
    private val entityMapper: ComponentMapper<EntityComponent> = ComponentMapper.getFor(EntityComponent::class.java)
    private val dimensionsMapper: ComponentMapper<DimensionsComponent> = ComponentMapper.getFor(DimensionsComponent::class.java)
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)

    private val LEFT = Direction.LEFT
    private val RIGHT = Direction.RIGHT
    private val UP = Direction.UP
    private val DOWN = Direction.DOWN

    private var engine: PooledEngine? = null

    private var spriterComponent: SpriterObjectComponent? = null
    private var playerComponent: PlayerComponent? = null
    private var transformComponent: TransformComponent? = null
    private var dimensionsComponent: DimensionsComponent? = null

    private val findWayTask = FindWayTask()
    private val playerPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
    private val goalPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)

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
            dimensionsComponent = ComponentRetriever.get(it, DimensionsComponent::class.java)

            spriterComponent?.animation?.startPlay()
        }
    }

    private fun movePlayer(direction: Int, autoRun: Boolean = false) {
        transformComponent?.apply {
            when (direction) {
                LEFT -> x -= 5f
                RIGHT -> x += 5f
                UP -> y += 5f
                DOWN -> y -= 5f
                LEFT + UP -> {
                    x -= 3.535f;y += 3.535f//3.535=Math.sqrt(12.5);5`2=2(x`2)
                }
                LEFT + DOWN -> {
                    x -= 3.535f;y -= 3.535f
                }
                RIGHT + UP -> {
                    x += 3.535f;y += 3.535f
                }
                RIGHT + DOWN -> {
                    x += 3.535f;y -= 3.535f
                }
            }
        }

        playerComponent?.apply {
            this.status = Status.RUN
            this.direction = direction
            this.isAutoRun = autoRun
        }

        dimensionsComponent?.let { d ->
            playerComponent?.goalEntity?.let { goal ->
                val eD = dimensionsMapper.get(goal)
                val overlap = eD.isOverlap(d.polygon)
                if (overlap) doAction()
            }
        }
    }

    private fun doAction() {
        playerComponent?.let { p ->
            val ec = p.goalEntity?.let { entityMapper.get(it) }
            ec?.isBeAttack = true
            p.goalType = ec?.info?.type ?: GoodsInfo.Type.UNKNOW.ordinal
            p.status = Status.ATTACK
        }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(LEFT + UP)
            Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(LEFT + DOWN)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W) && !Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(RIGHT + UP)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S) && !Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(RIGHT + DOWN)
            Gdx.input.isKeyPressed(Input.Keys.A) -> movePlayer(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) -> movePlayer(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) -> movePlayer(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) -> movePlayer(DOWN)
            else -> playerComponent?.status = Status.IDLE
        }

        playerComponent?.subStatus = if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) Status.ACTION else Status.NONE

        playerComponent?.let { player ->
            player.goalEntity?.let { transformMapper.get(it) }?.let {
                goalPosition.set(it.x, it.y)
            }
            transformComponent?.let { t ->
                if (player.subStatus == Status.ACTION && !player.isAutoRun && goalPosition.x != Float.NEGATIVE_INFINITY) {
                    playerPosition.set(t.x, t.y)
                    findWayTask.find(playerPosition, Vector2(goalPosition))
                }
                if (player.status == Status.RUN) findWayTask.interrupt()
            }
        }
    }

    override fun dispose() {
    }

    private inner class FindWayTask {
        private val obstacles: HashSet<Vector2> = HashSet<Vector2>(0)

        private val aStar = BidirectionalAStar()

        private var isFinding = false
        private var isRunning = false

        private val runJob = Job()

        fun find(start: Vector2, goal: Vector2) {
            Log.e("QL", "-------find way------>", isFinding, isRunning)
            if (isFinding || isRunning) return
            isFinding = true
            val rtn = aStar.searching(start, goal, obstacles)
            run(rtn.first)
            isFinding = false
        }

        fun interrupt() {
            isRunning = false
            trycatch { runJob.cancel() }
        }

        fun updateObstacles(value: HashSet<Vector2>) {
            obstacles.addAll(value)
        }

        private fun run(paths: LinkedList<Vector2>) {
            var oldP: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
            CoroutineScope(runJob).async(Dispatchers.IO) {
                isRunning = true
                for (path in paths) {
                    delay(33)
                    if (!isRunning) break
                    when {
                        path.y == oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT, true)
                        path.y == oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT, true)
                        path.x == oldP.x && path.y > oldP.y -> movePlayer(Direction.DOWN, true)
                        path.x == oldP.x && path.y < oldP.y -> movePlayer(Direction.UP, true)
                        path.y > oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT + Direction.DOWN, true)
                        path.y > oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT + Direction.DOWN, true)
                        path.y < oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT + Direction.UP, true)
                        path.y < oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT + Direction.UP, true)
                    }
                    oldP = path
                }
                isRunning = false
            }
        }
    }

}