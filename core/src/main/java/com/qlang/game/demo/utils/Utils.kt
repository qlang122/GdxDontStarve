package com.qlang.game.demo.utils

import com.badlogic.gdx.math.Polygon
import games.rednblack.editor.renderer.components.PolygonComponent
import games.rednblack.editor.renderer.utils.PolygonUtils

object Utils {
    fun makePolygon(polygonComponent: PolygonComponent): Polygon {
        val verticesArray = PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices)
        val vertices = FloatArray(verticesArray.size * 2)
        for (i in verticesArray.indices) {
            vertices[i * 2] = verticesArray[i].x
            vertices[i * 2 + 1] = verticesArray[i].y
        }
        return Polygon(vertices)
    }

    fun isOverlap(src: Polygon?, target: Polygon?): Boolean {
        if (src == null || target == null) return false

        var i = 0
        while (i < src.vertices.size) {
            if (target.contains(src.vertices[i], src.vertices[i + 1])) {
                return true
            }
            i += 2
        }
        i = 0
        while (i < target.vertices.size) {
            if (src.contains(target.vertices[i], target.vertices[i + 1])) {
                return true
            }
            i += 2
        }
        return false
    }
}