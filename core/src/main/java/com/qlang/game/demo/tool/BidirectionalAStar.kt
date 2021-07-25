package com.qlang.game.demo.tool

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class BidirectionalAStar {
    private var type: HeuristicType = HeuristicType.euclidean

    private val start: Point = Point(0, 0)
    private val goal: Point = Point(0, 0)

    private val obstacles: HashSet<Point> = HashSet<Point>(0)

    private val openFore: LinkedList<Point> = LinkedList()
    private val openBack: LinkedList<Point> = LinkedList()
    private val closeFore: LinkedList<Point> = LinkedList()
    private val closeBack: LinkedList<Point> = LinkedList()

    private val parentFore: HashMap<Point, Point> = HashMap()
    private val parentBack: HashMap<Point, Point> = HashMap()

    private val gFore: HashMap<Point, Float> = HashMap()
    private val gBack: HashMap<Point, Float> = HashMap()

    private val motions: Array<Point> = arrayOf(Point(-1, 0), Point(-1, 1),
            Point(0, 1), Point(1, 1), Point(1, 0),
            Point(1, -1), Point(0, -1), Point(-1, -1))

    companion object {
        enum class HeuristicType {
            euclidean, manhattan
        }
    }

    private fun init() {
        gFore.clear()
        gBack.clear()
        parentFore.clear()
        parentBack.clear()

        openFore.clear()
        openBack.clear()
        closeFore.clear()
        closeBack.clear()

        obstacles.clear()

        gFore[start] = 0.0f
        gFore[goal] = Float.POSITIVE_INFINITY
        gBack[goal] = 0.0f
        gBack[start] = Float.POSITIVE_INFINITY

        parentFore[start] = start
        parentBack[goal] = goal

        openFore.add(Point(start.x, start.y, valueFore(start)))
        openBack.add(Point(goal.x, goal.y, valueBack(goal)))

    }

    fun searching(start: Point, goal: Point, obstacles: HashSet<Point>, type: HeuristicType = HeuristicType.euclidean):
            Triple<LinkedList<Point>, LinkedList<Point>, LinkedList<Point>> {
        this.start.set(start)
        this.goal.set(goal)
        this.type = type
        this.obstacles.clear()
        this.obstacles.addAll(obstacles)
        init()

        var meet: Point = this.start

        while (openFore.isNotEmpty() && openBack.isNotEmpty()) {
            val fore = openFore.pop()
            if (parentBack have fore) {
                meet = Point(fore.x, fore.y)
                break
            }

            closeFore.add(Point(fore.x, fore.y))

            for (s_n in getNeighbor(fore)) {
                val v = Point(fore.x, fore.y)
                val newCost = gFore[v]?.plus(cost(v, s_n)) ?: 0f

                if (!gFore.any { (k, _) -> s_n.x == k.x && s_n.y == k.y })
                    gFore[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (gFore[s_n] ?: 0f)) {
                    gFore[s_n] = newCost
                    parentFore[s_n] = Point(fore.x, fore.y)
                    openFore.add(Point(s_n.x, s_n.y, valueFore(s_n)))
                }
            }

            val back = openBack.poll()
            if (parentFore have back) {
                meet = Point(back.x, back.y)
                break
            }

            closeBack.add(Point(back.x, back.y))

            for (s_n in getNeighbor(back)) {
                val v = Point(back.x, back.y)
                val newCost = gBack[v]?.plus(cost(v, s_n)) ?: 0f

                if (!gBack.any { (k, _) -> s_n.x == k.x && s_n.y == k.y })
                    gBack[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (gBack[s_n] ?: 0f)) {
                    gBack[s_n] = newCost
                    parentBack[s_n] = Point(back.x, back.y)
                    openBack.add(Point(s_n.x, s_n.y, valueFore(s_n)))
                }
            }
        }

        return Triple(extractPath(meet), closeFore, closeBack)
    }

    private fun getNeighbor(s: Point): LinkedList<Point> {
        return motions.mapTo(LinkedList(), { Point(it.x + s.x, it.y + s.y) })
    }

    private fun extractPath(meet: Point): LinkedList<Point> {
        val pathFore = LinkedList<Point>()

        pathFore.add(meet)
        var temp: Point? = meet

        while (true) {
            temp = parentFore[temp]
            temp?.let { pathFore.add(it) }
            if (temp?.x == start.x && temp.y == start.y) break
        }

        val pathBack = LinkedList<Point>()
        temp = meet

        while (true) {
            temp = parentBack[temp]
            temp?.let { pathBack.add(it) }
            if (temp?.x == goal.x && temp.y == goal.y) break
        }

        return LinkedList(pathFore.reversed()).apply { addAll(pathBack) }
    }

    private fun valueFore(value: Point): Float {
        return gFore[value]?.plus(h(value, goal)) ?: 0f
    }

    private fun valueBack(value: Point): Float {
        return gBack[value]?.plus(h(value, start)) ?: 0f
    }

    private fun h(s: Point, goal: Point): Float {
        return when (type) {
            HeuristicType.manhattan -> Math.abs(goal.x - s.x) + Math.abs(goal.y - s.y).toFloat()
            else -> Math.hypot((goal.x - s.x).plus(0.0), (goal.y - s.y).plus(0.0)).toFloat()
        }
    }

    private fun cost(start: Point, goal: Point): Float {
        if (isCollision(start, goal)) return Float.POSITIVE_INFINITY
        return Math.hypot((goal.x - start.x).plus(0.0), (goal.y - start.y).plus(0.0)).toFloat()
    }

    private fun isCollision(start: Point, end: Point): Boolean {
        if (obstacles have start || obstacles have end) return true

        if (start.x != end.x && start.y != end.y) {
            val s1: Point
            val s2: Point
            if (end.x - start.x == start.y - end.y) {
                s1 = Point(Math.min(start.x, end.x), Math.min(start.y, end.y))
                s2 = Point(Math.max(start.x, end.x), Math.max(start.y, end.y))
            } else {
                s1 = Point(Math.min(start.x, end.x), Math.max(start.y, end.y))
                s2 = Point(Math.max(start.x, end.x), Math.min(start.y, end.y))
            }
            if (obstacles have s1 || obstacles have s2)
                return true
        }
        return false
    }

    infix fun HashSet<Point>.have(value: Point): Boolean {
        return this.any { it.x == value.x && it.y == value.y }
    }

    infix fun HashMap<Point, Point>.have(value: Point): Boolean {
        return this.any { (_, it) -> it.x == value.x && it.y == value.y }
    }
}