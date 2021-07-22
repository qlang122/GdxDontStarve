package com.qlang.game.demo

import com.badlogic.gdx.math.Vector2
import com.qlang.game.demo.tool.BidirectionalAStar
import org.junit.Test
import java.util.*
import kotlin.collections.HashSet

class Test11 {
    @Test
    fun test() {
        val aStar = BidirectionalAStar()

        val start = Vector2(2f, 2f)
        val goal = Vector2(19f, 12f)
        val obstacles = HashSet<Vector2>()
        for (i in 0..10) obstacles.add(Vector2(6f, 0f + i))
        for (i in 0..18) obstacles.add(Vector2(13f + i, 8f))
        for (i in 0..13) obstacles.add(Vector2(16f, 4f + i))

        val st = System.currentTimeMillis()
        val searching = aStar.searching(start, goal, obstacles)
        println("---time-->>${System.currentTimeMillis() - st}")
        println(searching.first)

        val map = Array<IntArray>(60) { IntArray(40) { 0 } }
        for (it in searching.first) {
            val x = it.x.toInt()
            val y = it.y.toInt()
            if (x > 0 && y > 0) map[y][x] = 1
        }
//        for (it in searching.second) {
//            map[it.x.toInt()][it.y.toInt()] = 6
//        }
//        for (it in searching.third) {
//            map[it.x.toInt()][it.y.toInt()] = 9
//        }
        for (it in obstacles) {
            map[it.y.toInt()][it.x.toInt()] = 7
        }

        for (it in map) {
            for (i in it) {
                print("$i")
            }
            println()
        }

    }
}