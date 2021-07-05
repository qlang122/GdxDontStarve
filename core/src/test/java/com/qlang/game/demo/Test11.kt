package com.qlang.game.demo

import com.badlogic.gdx.math.Matrix4
import org.junit.Test

class Test11 {
    @Test
    fun test() {
        val projection = Matrix4()
        val viewportWidth = 1920f
        val viewportHeight = 1080f
        val zoom = 1f
        val near = 0f
        val far = 100f
        val aspect = viewportWidth / viewportHeight
        //        projection.setToProjection(Math.abs(1f), Math.abs(far), 67f, aspect);
        projection.setToOrtho(1 * -viewportWidth / 2, zoom * (viewportWidth / 2), zoom * -(viewportHeight / 2), zoom
                * viewportHeight / 2, near, far)
        println(projection)

        for (i in 5..359 step 5) {
            val l_fd = (1.0 / Math.tan(i * (Math.PI / 180) / 2.0)).toFloat()
            println(l_fd)
        }
    }
}