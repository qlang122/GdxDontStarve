package com.qlang.game.demo

import com.qlang.game.demo.ktx.execAsync
import com.qlang.game.demo.tool.BidirectionalAStar
import com.qlang.game.demo.tool.Point
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import kotlin.collections.HashSet

class Test11 {
    @Test
    fun test() {
        val aStar = BidirectionalAStar()

        val start = Point(0, 0)//51,-410
        val goal = Point(27, -17)//168,-571
        val obstacles = HashSet<Point>()
//        for (i in 0..10) obstacles.add(Vector2(6f, 0f + i))
//        for (i in 0..18) obstacles.add(Vector2(13f + i, 8f))
//        for (i in 0..13) obstacles.add(Vector2(16f, 4f + i))

        val st = System.currentTimeMillis()
        val searching = aStar.searching(start, goal, obstacles)
        println("---time-->>${System.currentTimeMillis() - st}")
        println(searching.first)

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
}