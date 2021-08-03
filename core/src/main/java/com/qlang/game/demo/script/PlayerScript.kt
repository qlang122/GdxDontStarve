package com.qlang.game.demo.script

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.qlang.game.demo.component.EntityComponent
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.Direction
import com.qlang.game.demo.res.R
import com.qlang.game.demo.res.Status
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.utils.Utils
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import com.qlang.pathplanning.AStar
import com.qlang.pathplanning.Point
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.PolygonComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


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
    private var physicsBodyComponent: PhysicsBodyComponent? = null

    private var playerPolygon: Polygon? = null

    private val findWayTask = FindWayTask()
    private val playerPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)
    private val goalPosition: Vector2 = Vector2(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY)

    private var playerManager: PlayerManager? = null

    private val impulse = Vector2(0f, 0f)
    private val speed = Vector2(0f, 0f)

    constructor(engine: PooledEngine) {
        this.engine = engine
    }

    override fun init(item: Entity?) {
        super.init(item)

        item?.let {
            playerManager = PlayerManager(it)
            playerManager?.loadAssets()

            playerPolygon = it.getComponent(PolygonComponent::class.java)?.let { Utils.makePolygon(it) }

            ItemWrapper(it).getChild("role")?.entity?.let { role ->
                spriterComponent = ComponentRetriever.get(role, SpriterObjectComponent::class.java)
                playerComponent = ComponentRetriever.get(role, PlayerComponent::class.java)
            }
            transformComponent = ComponentRetriever.get(it, TransformComponent::class.java)
            dimensionsComponent = ComponentRetriever.get(it, DimensionsComponent::class.java)
            physicsBodyComponent = ComponentRetriever.get(it, PhysicsBodyComponent::class.java)

            spriterComponent?.animation?.startPlay()
        }

        playerComponent?.addAnimChangeListener {
            it?.let {
                playerManager?.run {
                    swapObject(currSwapObject, true)
                    swapHat(currSwapHat, true)
                    swapBody(currSwapBody, true)
                }
            }
        }
    }

    private fun isOverlap(): Boolean {
        playerComponent?.let { p ->
            val ee = p.goalEntity?.let { g -> entityMapper.get(g) }
//            Log.e("QL", "----->>$overlap", ee?.polygon?.boundingRectangle, playerPolygon?.boundingRectangle)
            return ee?.polygon?.boundingRectangle?.overlaps(playerPolygon?.boundingRectangle)
                    ?: false
        }
        return false
    }

    private fun movePlayer(direction: Int, autoRun: Boolean = false) {
        transformComponent?.let { playerPolygon?.setPosition(it.x, it.y) }

        physicsBodyComponent?.body?.let { body ->
            speed.set(body.linearVelocity)
            when (direction) {
                LEFT -> impulse.set(-100000f, speed.y)
                RIGHT -> impulse.set(100000f, speed.y)
                UP -> impulse.set(speed.x, 100000f)
                DOWN -> impulse.set(speed.x, -100000f)
                LEFT + UP -> impulse.set(-100000f, 100000f)
                LEFT + DOWN -> impulse.set(-100000f, -100000f)
                RIGHT + UP -> impulse.set(100000f, 100000f)
                RIGHT + DOWN -> impulse.set(100000f, -100000f)
            }
            body.applyLinearImpulse(impulse.sub(speed), body.worldCenter, true)
        }

        val overlap = isOverlap()
        if (overlap && playerComponent?.isAutoRun == true) {
            findWayTask.interrupt()
            doAction()
        } else if (overlap && playerComponent?.subStatus == Status.ACTION) {
            doAction()
        } else {
            playerComponent?.apply {
                this.isAutoRun = autoRun
                this.direction = direction
                this.status = Status.RUN
            }
        }
    }

    private fun doAction() {
        playerComponent?.let { p ->
            val ec = p.goalEntity?.let { entityMapper.get(it) }
            ec?.isBeAttack = true
            p.goalType = ec?.info?.type ?: GoodsInfo.Type.UNKNOW.value
            p.status = Status.ATTACK
        }
    }

    override fun act(delta: Float) {
        when {
            Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.W)
                    && !Gdx.input.isKeyPressed(Input.Keys.S) -> activeMove(LEFT + UP)
            Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.S)
                    && !Gdx.input.isKeyPressed(Input.Keys.W) -> activeMove(LEFT + DOWN)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.W)
                    && !Gdx.input.isKeyPressed(Input.Keys.S) -> activeMove(RIGHT + UP)
            Gdx.input.isKeyPressed(Input.Keys.D) && Gdx.input.isKeyPressed(Input.Keys.S)
                    && !Gdx.input.isKeyPressed(Input.Keys.W) -> activeMove(RIGHT + DOWN)
            Gdx.input.isKeyPressed(Input.Keys.A) -> activeMove(LEFT)
            Gdx.input.isKeyPressed(Input.Keys.D) -> activeMove(RIGHT)
            Gdx.input.isKeyPressed(Input.Keys.W) -> activeMove(UP)
            Gdx.input.isKeyPressed(Input.Keys.S) -> activeMove(DOWN)
            Gdx.input.isKeyPressed(Input.Keys.R) -> playerManager?.swapObject(R.anim.player.swap_glassaxe)
            else -> if (playerComponent?.isAutoRun != true) playerComponent?.status = Status.IDLE
        }

        playerComponent?.subStatus = if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) Status.ACTION else Status.NONE

        playerComponent?.let { player ->
            player.goalEntity?.let {
                val d = dimensionsMapper.get(it)
                transformMapper.get(it)?.let { t ->
                    goalPosition.set(t.x + (d?.width ?: 0f) / 2, t.y + (d?.height ?: 0f) / 2)
                }
            }
            transformComponent?.let { t ->
                playerPosition.set(t.x + (dimensionsComponent?.width ?: 0f) / 2,
                        t.y + (dimensionsComponent?.height ?: 0f) / 2)
//                Log.e("QL", "--->", player.subStatus, !player.isAutoRun, goalPosition.x)
                val d = abs(sqrt(goalPosition.x.minus(playerPosition.x).pow(2) + goalPosition.y.minus(playerPosition.y).pow(2)))
//                Log.e("QL", "------------>>", d)
                if (player.subStatus == Status.ACTION && !player.isAutoRun && d < 250) {
                    if (isOverlap() || d < 15) doAction()
                    else findWayTask.find(playerPosition, Vector2(goalPosition))
                }
            }
        }
    }

    private fun activeMove(direction: Int) {
        playerComponent?.let { if (it.status == Status.RUN && it.isAutoRun) findWayTask.interrupt() }
        movePlayer(direction)
    }

    override fun dispose() {
    }

    private inner class FindWayTask : Runnable {
        private val obstacles: HashSet<Point> = HashSet(0)

        private val aStar = AStar()

        private var isFinding = false
        private var isRunning = false

        private var thread: Thread? = null

        private val start = Point(0, 0)
        private val goal = Point(0, 0)

        fun find(start: Vector2, goal: Vector2) {
            if (isFinding || isRunning) return

//            val s = Point(0, 0)//以player为原点
            val g = Point((goal.x - start.x).div(5f).toInt(), (goal.y - start.y).div(5f).toInt())
            Log.e("QL", "-----find way---->", start, goal, g)
            this.start.set(0, 0)
            this.goal.set(g.x, g.y)

            thread = Thread(this)
            thread?.start()
        }

        fun interrupt() {
            if (isRunning) trycatch { thread?.interrupt() }
            isFinding = false
            isRunning = false
        }

        fun updateObstacles(value: HashSet<Point>) {
            obstacles.addAll(value)
        }

        override fun run() {
            isFinding = true
            val st = System.currentTimeMillis()
            val rtn = aStar.searching(this.start, this.goal, obstacles)
            Log.e("QL", "----find way---->${System.currentTimeMillis() - st} ${rtn.first.size}")
            isFinding = false
            run(rtn.first)
        }

        private fun run(paths: LinkedList<Point>) {
            if (Thread.interrupted()) return

            val oldP = Point(Int.MAX_VALUE, Int.MAX_VALUE)
            isRunning = true
            for (path in paths) {
                if (!isRunning || Thread.interrupted()) break
                val y = path.y
                val x = path.x
                when {
                    y == oldP.y && x > oldP.x -> move(Direction.LEFT)//方位全部取反，因为点从目标到起点，反方向的
                    y == oldP.y && x < oldP.x -> move(Direction.RIGHT)
                    x == oldP.x && y > oldP.y -> move(Direction.DOWN)
                    x == oldP.x && y < oldP.y -> move(Direction.UP)
                    y > oldP.y && x > oldP.x -> move(Direction.LEFT + Direction.DOWN)
                    y > oldP.y && x < oldP.x -> move(Direction.RIGHT + Direction.DOWN)
                    y < oldP.y && x > oldP.x -> move(Direction.LEFT + Direction.UP)
                    y < oldP.y && x < oldP.x -> move(Direction.RIGHT + Direction.UP)
                }
                oldP.set(x, y)
                trycatch { Thread.sleep(17) }
            }
            playerComponent?.status = Status.IDLE
            playerComponent?.isAutoRun = false
            isRunning = false
        }

        private fun move(direction: Int) {
            movePlayer(direction, true)
        }
    }

}