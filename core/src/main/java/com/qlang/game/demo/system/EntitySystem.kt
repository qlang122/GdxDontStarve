package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.Status
import com.qlang.game.demo.tool.BidirectionalAStar
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

open class EntitySystem : IteratingSystem(Family.all(EntityComponent::class.java).get()) {
    private val mainItemMapper: ComponentMapper<MainItemComponent> = ComponentMapper.getFor(MainItemComponent::class.java)
    private val entityMapper: ComponentMapper<EntityComponent> = ComponentMapper.getFor(EntityComponent::class.java)
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val dimensionsMapper: ComponentMapper<DimensionsComponent> = ComponentMapper.getFor(DimensionsComponent::class.java)

    private var playerComponent: PlayerComponent? = null
    private var playerTransformComponent: TransformComponent? = null

    private var player: Entity? = null

    private val findWayTask = FindWayTask()
    private val playerPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
    private val goalPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
    private var goalEntity: Entity? = null
    private var minAbs: Double = Double.NEGATIVE_INFINITY

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (player == null || playerComponent == null) return

        val m = mainItemMapper.get(entity)
        if (!m.culled) {
            val t = transformMapper.get(entity)
            val d = dimensionsMapper.get(entity)
            val ec = entityMapper.get(entity)
            if (ec.info.type == GoodsInfo.Type.UNKNOW.ordinal) return

            val pT = transformMapper.get(player)
            val pD = dimensionsMapper.get(player)

            val abs = abs(sqrt(pT.x.minus(t.x + 0.0).pow(2.0) + pT.y.minus(t.y + 0.0).pow(2.0)))
            ec.playerDistance = abs.toFloat()
            if (abs < 150) {
                val overlap = d.isOverlap(pD.polygon)
                ec.isOverlapPlayer = overlap

                if (overlap) {
                    goalEntity = entity
                    doAction()
                } else {
                    if (abs < minAbs) {
                        minAbs = abs;goalPosition.set(t.x, t.y);goalEntity = entity
                    }

                    playerComponent?.let { player ->
                        if (player.subStatus == Status.ACTION && !player.isAutoRun) {
                            playerPosition.set(pT.x, pT.y)
                            findWayTask.find(playerPosition, Vector2(goalPosition))
                        }
                        if (player.status == Status.RUN) findWayTask.interrupt()
                    }
                }
            }

        }
    }

    fun setPlayer(player: Entity?) {
        this.player = player
        player?.let {
            playerComponent = ComponentRetriever.get(it, PlayerComponent::class.java)
            playerTransformComponent = ComponentRetriever.get(it, TransformComponent::class.java)
        }
    }

    private fun movePlayer(direction: Int) {
        playerTransformComponent?.apply {
            when (direction) {
                Direction.LEFT -> x -= 5f
                Direction.RIGHT -> x += 5f
                Direction.UP -> y += 5f
                Direction.DOWN -> y -= 5f
                Direction.LEFT + Direction.UP -> {
                    x -= 1.581f;y -= 1.581f//1.581=Math.sqrt(2.5);5=2(x*x)
                }
                Direction.LEFT + Direction.DOWN -> {
                    x -= 1.581f;y += 1.581f
                }
                Direction.RIGHT + Direction.UP -> {
                    x += 1.581f;y -= 1.581f
                }
                Direction.RIGHT + Direction.DOWN -> {
                    x += 1.581f;y += 1.581f
                }
            }
        }

        playerComponent?.apply {
            this.status = Status.RUN
            this.direction = direction
            this.isAutoRun = true
        }

        doAction()
    }

    private fun doAction() {
        goalEntity?.let {
            val ec = entityMapper.get(it)
            val d = dimensionsMapper.get(it)
            val pD = dimensionsMapper.get(player)
            val overlap = d?.isOverlap(pD.polygon)
            if (overlap == true) {
                findWayTask.interrupt()
                ec.isBeAttack = true
                playerComponent?.goalType = ec.info.type
                playerComponent?.status = Status.ATTACK
            }
        }
    }

    private inner class FindWayTask {
        private val obstacles: HashSet<Vector2> = HashSet<Vector2>(0)

        private val aStar = BidirectionalAStar()

        private var isFinding = false
        private var isRunning = false

        private val runJob = Job()

        fun find(start: Vector2, goal: Vector2) {
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
                        path.y == oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT)
                        path.y == oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT)
                        path.x == oldP.x && path.y > oldP.y -> movePlayer(Direction.DOWN)
                        path.x == oldP.x && path.y < oldP.y -> movePlayer(Direction.UP)
                        path.y > oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT + Direction.DOWN)
                        path.y > oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT + Direction.DOWN)
                        path.y < oldP.y && path.x > oldP.x -> movePlayer(Direction.RIGHT + Direction.UP)
                        path.y < oldP.y && path.x < oldP.x -> movePlayer(Direction.LEFT + Direction.UP)
                    }
                    oldP = path
                }
                isRunning = false
            }
        }
    }
}