package com.qlang.game.demo.component

import com.badlogic.gdx.math.Polygon
import com.qlang.game.demo.entity.GoodsInfo
import com.qlang.game.demo.res.Status
import games.rednblack.editor.renderer.components.BaseComponent
import games.rednblack.editor.renderer.components.PolygonComponent
import games.rednblack.editor.renderer.utils.PolygonUtils

class EntityComponent : BaseComponent {
    var polygon: Polygon? = null
        private set

    val info: GoodsInfo = GoodsInfo()
    var state: Int = Status.IDLE

    var playerDistance: Float = Float.NEGATIVE_INFINITY
    var isOverlapPlayer: Boolean = false

    var isBeAttack = false

    override fun reset() {
        info.reset()
    }

    fun setPolygon(polygonComponent: PolygonComponent) {
        val verticesArray = PolygonUtils.mergeTouchingPolygonsToOne(polygonComponent.vertices)
        val vertices = FloatArray(verticesArray.size * 2)
        for (i in verticesArray.indices) {
            vertices[i * 2] = verticesArray[i].x
            vertices[i * 2 + 1] = verticesArray[i].y
        }
        polygon = Polygon(vertices)
    }

}