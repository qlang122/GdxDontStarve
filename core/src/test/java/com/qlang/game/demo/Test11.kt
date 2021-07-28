package com.qlang.game.demo

import com.qlang.game.demo.ktx.execAsync
import com.qlang.pathplanning.AStar
import com.qlang.pathplanning.Point
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.junit.Test
import java.util.*
import kotlin.collections.HashSet

class Test11 {
    @Test
    fun test() {
//        val aStar = BidirectionalAStar()
        val aStar = AStar()

        val start = Point(0, -5)
        val goal = Point(20, 25)
        val obstacles = HashSet<Point>()
        for (i in 0..10) obstacles.add(Point(6, -3 + i))
        for (i in 0..18) obstacles.add(Point(-6 + i, 8))
        for (i in 0..13) obstacles.add(Point(16, 4 + i))

        val st = System.currentTimeMillis()
        val searching = aStar.searching(start, goal, obstacles)
        println("---time-->>${System.currentTimeMillis() - st}")
        val paths = searching.first
//        println(paths)

        for (i in -40..40) {
            for (j in -40..40) {
                var have = false
                for (p in paths) {
                    if (j == p.x && i == p.y) {
                        print("1")
                        have = true
                        break
                    }
                }
                for (a in obstacles) {
                    if (j == a.x && i == a.y) {
                        print("7")
                        have = true
                        break
                    }
                }
                if (have) continue
                print("0")
            }
            println()
        }

    }

    @Test
    fun test2() {
        val job = Job()
        execAsync({
            println("-----run------")
            for (i in 0..10) {
                delay(500L)
                println("-----run---1---")
            }
        }, {
            println("----------------------e")
        }, job)
        Thread.sleep(2000)
        job.cancel()
        Thread.sleep(2000)
    }

    @Test
    fun test3() {
        val list = PriorityQueue<Point>()
        list.add(Point(0, 0, 1.0f))
        list.add(Point(0, 0, 2.0f))
        list.add(Point(0, 0, 4.0f))
        list.add(Point(0, 0, 1.0f))
        list.add(Point(0, 0, 3.0f))
        list.add(Point(0, 0, 7.0f))

        println(list)
        println(list.poll())
        println(list)
        println(list.poll())
        println(list)
        println(list.poll())
        println(list)

    }
}