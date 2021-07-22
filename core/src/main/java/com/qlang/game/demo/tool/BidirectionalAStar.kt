package com.qlang.game.demo.tool

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class BidirectionalAStar {
    private var type: HeuristicType = HeuristicType.euclidean

    private val start: Vector2 = Vector2(0f, 0f)
    private val goal: Vector2 = Vector2(0f, 0f)

    private val obstacles: HashSet<Vector2> = HashSet<Vector2>(0)

    private val openFore: LinkedList<Vector3> = LinkedList()
    private val openBack: LinkedList<Vector3> = LinkedList()
    private val closeFore: LinkedList<Vector2> = LinkedList()
    private val closeBack: LinkedList<Vector2> = LinkedList()

    private val parentFore: HashMap<Vector2, Vector2> = HashMap()
    private val parentBack: HashMap<Vector2, Vector2> = HashMap()

    private val gFore: HashMap<Vector2, Float> = HashMap()
    private val gBack: HashMap<Vector2, Float> = HashMap()

    private val motions: Array<Vector2> = arrayOf(Vector2(-1f, 0f), Vector2(-1f, 1f),
            Vector2(0f, 1f), Vector2(1f, 1f), Vector2(1f, 0f),
            Vector2(1f, -1f), Vector2(0f, -1f), Vector2(-1f, -1f))

    companion object {
        enum class HeuristicType {
            euclidean, manhattan
        }
    }

    private fun init() {
        gFore[start] = 0.0f
        gFore[goal] = Float.POSITIVE_INFINITY
        gBack[goal] = 0.0f
        gBack[start] = Float.POSITIVE_INFINITY

        parentFore[start] = start
        parentBack[goal] = goal

        openFore.add(Vector3(start.x, start.y, valueFore(start)))
        openBack.add(Vector3(goal.x, goal.y, valueBack(goal)))

    }

    fun searching(start: Vector2, goal: Vector2, obstacles: HashSet<Vector2>, type: HeuristicType = HeuristicType.euclidean):
            Triple<LinkedList<Vector2>, LinkedList<Vector2>, LinkedList<Vector2>> {
        this.start.set(start)
        this.goal.set(goal)
        this.type = type
        this.obstacles.clear()
        this.obstacles.addAll(obstacles)
        init()

        var meet: Vector2 = this.start

        while (openFore.isNotEmpty() && openBack.isNotEmpty()) {
            val fore = openFore.pop()
            if (parentBack have fore) {
                meet = Vector2(fore.x, fore.y)
                break
            }

            closeFore.add(Vector2(fore.x, fore.y))

            for (s_n in getNeighbor(fore)) {
                val v = Vector2(fore.x, fore.y)
                val newCost = gFore[v]?.plus(cost(v, s_n)) ?: 0f

                if (!gFore.any { (k, _) -> s_n.x == k.x && s_n.y == k.y })
                    gFore[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (gFore[s_n] ?: 0f)) {
                    gFore[s_n] = newCost
                    parentFore[s_n] = Vector2(fore.x, fore.y)
                    openFore.add(Vector3(s_n.x, s_n.y, valueFore(s_n)))
                }
            }

            val back = openBack.poll()
            if (parentFore have back) {
                meet = Vector2(back.x, back.y)
                break
            }

            closeBack.add(Vector2(back.x, back.y))

            for (s_n in getNeighbor(back)) {
                val v = Vector2(back.x, back.y)
                val newCost = gBack[v]?.plus(cost(v, s_n)) ?: 0f

                if (!gBack.any { (k, _) -> s_n.x == k.x && s_n.y == k.y })
                    gBack[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (gBack[s_n] ?: 0f)) {
                    gBack[s_n] = newCost
                    parentBack[s_n] = Vector2(back.x, back.y)
                    openBack.add(Vector3(s_n.x, s_n.y, valueFore(s_n)))
                }
            }
        }

        return Triple(extractPath(meet), closeFore, closeBack)
    }

    private fun getNeighbor(s: Vector3): LinkedList<Vector2> {
        return motions.mapTo(LinkedList(), { Vector2(it.x + s.x, it.y + s.y) })
    }

    private fun extractPath(meet: Vector2): LinkedList<Vector2> {
        val pathFore = LinkedList<Vector2>()

        pathFore.add(meet)
        var temp: Vector2? = meet

        while (true) {
            temp = parentFore[temp]
            temp?.let { pathFore.add(it) }
            if (temp?.x == start.x && temp.y == start.y) break
        }

        val pathBack = LinkedList<Vector2>()
        temp = meet

        while (true) {
            temp = parentBack[temp]
            temp?.let { pathBack.add(it) }
            if (temp?.x == goal.x && temp.y == goal.y) break
        }

        return LinkedList(pathFore.reversed()).apply { addAll(pathBack) }
    }

    private fun valueFore(value: Vector2): Float {
        return gFore[value]?.plus(h(value, goal)) ?: 0f
    }

    private fun valueBack(value: Vector2): Float {
        return gBack[value]?.plus(h(value, start)) ?: 0f
    }

    private fun h(s: Vector2, goal: Vector2): Float {
        return when (type) {
            HeuristicType.manhattan -> Math.abs(goal.x - s.x) + Math.abs(goal.y - s.y)
            else -> Math.hypot((goal.x - s.x).plus(0.0), (goal.y - s.y).plus(0.0)).toFloat()
        }
    }

    private fun cost(start: Vector2, goal: Vector2): Float {
        if (isCollision(start, goal)) return Float.POSITIVE_INFINITY
        return Math.hypot((goal.x - start.x).plus(0.0), (goal.y - start.y).plus(0.0)).toFloat()
    }

    private fun isCollision(start: Vector2, end: Vector2): Boolean {
        if (obstacles have start || obstacles have end) return true

        if (start.x != end.x && start.y != end.y) {
            val s1: Vector2
            val s2: Vector2
            if (end.x - start.x == start.y - end.y) {
                s1 = Vector2(Math.min(start.x, end.x), Math.min(start.y, end.y))
                s2 = Vector2(Math.max(start.x, end.x), Math.max(start.y, end.y))
            } else {
                s1 = Vector2(Math.min(start.x, end.x), Math.max(start.y, end.y))
                s2 = Vector2(Math.max(start.x, end.x), Math.min(start.y, end.y))
            }
            if (obstacles have s1 || obstacles have s2)
                return true
        }
        return false
    }

    infix fun HashSet<Vector2>.have(value: Vector2): Boolean {
        return this.any { it.x == value.x && it.y == value.y }
    }

    infix fun HashMap<Vector2, Vector2>.have(value: Vector3): Boolean {
        return this.any { (_, it) -> it.x == value.x && it.y == value.y }
    }
}