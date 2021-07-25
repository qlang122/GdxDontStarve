package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.qlang.game.demo.utils.Log
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.ViewPortComponent
import kotlin.math.max
import kotlin.math.min

class CameraSystem(val xMin: Float, val xMax: Float, val yMin: Float, val yMax: Float)
    : IteratingSystem(Family.all(ViewPortComponent::class.java).get()) {
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val viewPortMapper: ComponentMapper<ViewPortComponent> = ComponentMapper.getFor(ViewPortComponent::class.java)
    private val dimensionsMapper: ComponentMapper<DimensionsComponent> = ComponentMapper.getFor(DimensionsComponent::class.java)

    private var focus: Entity? = null

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        focus?.let {
            val t = transformMapper.get(it)
            val d = dimensionsMapper.get(it)
            if (t == null) return

            val camera: Camera? = viewPortMapper.get(entity)?.viewPort?.camera

            val x = max(xMin, min(xMax, t.x))
            val y = max(yMin, min(yMax, t.y + 2))
//            Log.e("QL", "----->", x, y, it.x, it.y)
            camera?.position?.set(x + (d?.width ?: 0f) / 2, y + 50f, 0f)
        }
    }

    fun setFocus(focus: Entity?) {
        this.focus = focus
    }
}