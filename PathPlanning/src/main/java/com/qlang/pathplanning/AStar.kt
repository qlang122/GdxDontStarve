package com.qlang.pathplanning

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class AStar {
    private var type: HeuristicType = HeuristicType.euclidean

    private val start: Point = Point(0, 0)
    private val goal: Point = Point(0, 0)

    private val obstacles: HashSet<Point> = HashSet<Point>(0)

    private val OPEN: PriorityQueue<Point> = PriorityQueue()

    private val CLOSE: LinkedList<Point> = LinkedList()
    private val parent: HashMap<Point, Point> = HashMap()

    private val gFore: HashMap<Point, Float> = HashMap()

    private val motions: Array<Point> = arrayOf(Point(-1, 0), Point(-1, 1),
            Point(0, 1), Point(1, 1), Point(1, 0),
            Point(1, -1), Point(0, -1), Point(-1, -1))

    private fun init() {
        gFore.clear()
        parent.clear()

        OPEN.clear()
        CLOSE.clear()

        obstacles.clear()

        gFore[start] = 0.0f
        gFore[goal] = Float.POSITIVE_INFINITY

        parent[start] = start

        OPEN.add(Point(start.x, start.y, fValue(start)))
    }

    fun searching(start: Point, goal: Point, obstacles: HashSet<Point>, type: HeuristicType = HeuristicType.euclidean):
            Pair<LinkedList<Point>, LinkedList<Point>> {
        this.start.set(start)
        this.goal.set(goal)
        this.type = type
        init()
        this.obstacles.addAll(obstacles)

        while (OPEN.isNotEmpty()) {
            val fore = OPEN.poll() ?: continue
            CLOSE.add(Point(fore.x, fore.y))

            if (fore.x == this.goal.x && fore.y == this.goal.y) break

            for (s_n in getNeighbor(fore)) {
                val v = Point(fore.x, fore.y)
                val newCost = gFore[v]?.plus(cost(v, s_n)) ?: 0f

                if (!gFore.containsKey(s_n))
                    gFore[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (gFore[s_n] ?: 0f)) {
                    gFore[s_n] = newCost
                    parent[s_n] = Point(fore.x, fore.y)
                    OPEN.add(Point(s_n.x, s_n.y, fValue(s_n)))
                }
            }
        }
        return Pair(extractPath(parent), CLOSE)
    }

    fun searchingRepeated(e: Float): Pair<LinkedList<Point>, LinkedList<Point>> {
        val path = LinkedList<Point>()
        val visited = LinkedList<Point>()
        var e = e
        while (e >= 1) {
            val v = repeatedSearching(start, goal, e)
            path.addAll(v.first)
            visited.addAll(v.second)
            e -= 0.5f
        }
        return Pair(path, visited)
    }

    private fun repeatedSearching(start: Point, goal: Point, e: Float): Pair<LinkedList<Point>, LinkedList<Point>> {
        val g = HashMap<Point, Float>()
        g[start] = 0f
        g[goal] = Float.POSITIVE_INFINITY

        val parent = HashMap<Point, Point>()
        parent[start] = start

        val open = PriorityQueue<Point>()
        val close = LinkedList<Point>()

        open.add(Point(start.x, start.y, g[start]?.plus(h(start, goal)) ?: 0f))

        while (open.isNotEmpty()) {
            val s = open.poll() ?: continue
            close.push(Point(s.x, s.y))

            if (s.x == goal.x && s.y == goal.y) break

            for (s_n in getNeighbor(s)) {
                val v = Point(s.x, s.y)
                val newCost = g[v]?.plus(cost(v, s_n)) ?: 0f

                if (!g.containsKey(s_n))
                    g[s_n] = Float.POSITIVE_INFINITY

                if (newCost < (g[s_n] ?: 0f)) {
                    g[s_n] = newCost
                    parent[s_n] = Point(s.x, s.y)
                    open.add(Point(s_n.x, s_n.y, g[s_n]?.plus(e * h(s_n, this.goal)) ?: 0f))
                }
            }
        }
        return Pair(extractPath(parent), close)
    }

    private fun getNeighbor(s: Point): LinkedList<Point> {
        return motions.mapTo(LinkedList(), { Point(it.x + s.x, it.y + s.y) })
    }

    private fun extractPath(parent: HashMap<Point, Point>): LinkedList<Point> {
        val path = LinkedList<Point>()

        path.add(goal)
        var temp: Point? = goal

        while (true) {
            temp = parent[temp]
            temp?.let { path.add(it) }
            if (temp == start) break
        }

        return path
    }

    private fun fValue(value: Point): Float {
        return gFore[value]?.plus(h(value, goal)) ?: 0f
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
}