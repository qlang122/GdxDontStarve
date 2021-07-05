package com.qlang.game.demo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.ViewPortComponent
import games.rednblack.editor.renderer.utils.ComponentRetriever

class TiltWorldSystem : IteratingSystem(Family.all(ViewPortComponent::class.java).get()) {
    private var oldScaleX = -1f
    private var oldScaleY = -1f

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        ComponentRetriever.get(entity, TransformComponent::class.java)?.let { t ->
            if (oldScaleX == -1f) oldScaleX = t.scaleX
            if (oldScaleY == -1f) oldScaleY = t.scaleY
            ComponentRetriever.get(entity, ViewPortComponent::class.java)?.viewPort?.camera?.let { camera ->
                val h = camera.viewportHeight.plus(300).div(2)
                val fl = t.y - (camera.position.y - h)
                if (fl > 0) {
                    val s = fl / camera.viewportHeight * 0.3f
                    t.scaleX = oldScaleX - s
                    t.scaleY = oldScaleX - s
                } else {
                    t.scaleX = oldScaleX
                    t.scaleY = oldScaleY
                }
            }
        }
    }
}