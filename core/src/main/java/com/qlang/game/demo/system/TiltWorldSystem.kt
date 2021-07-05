package com.qlang.game.demo.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.qlang.game.demo.component.ScaleEntityComponent
import games.rednblack.editor.renderer.components.*
import games.rednblack.editor.renderer.utils.ComponentRetriever
import kotlin.math.abs

class TiltWorldSystem : IteratingSystem {
    private var viewOffset = 0.25f
    private var viewportHeight: Float = 0f

    private var focus: Entity? = null

    constructor() : this(0.25f)

    constructor(viewOffset: Float) : super(Family.all(ScaleEntityComponent::class.java).get()) {
        this.viewOffset = viewOffset
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (focus == null) return

        val pT = ComponentRetriever.get(focus, TransformComponent::class.java)
        val transform = ComponentRetriever.get(entity, TransformComponent::class.java)
        val scale = ComponentRetriever.get(entity, ScaleEntityComponent::class.java)?.apply {
            if (oldScaleX == -1f) oldScaleX = transform.scaleX
            if (oldScaleY == -1f) oldScaleY = transform.scaleY
        }

        val m = ComponentRetriever.get(entity, MainItemComponent::class.java)
        val fl = transform.y - pT.y
        val s = abs(fl) / viewportHeight / 2f * viewOffset

        if (!m.culled) {
            when {
                fl > 0 -> {
                    scale?.oldScaleX?.minus(s)?.let { transform.scaleX = it }
                    scale?.oldScaleY?.minus(s)?.let { transform.scaleY = it }
                }
                fl < 0 -> {
                    scale?.oldScaleX?.plus(s)?.let { transform.scaleX = it }
                    scale?.oldScaleY?.plus(s)?.let { transform.scaleY = it }
                }
            }
        } else {
            scale?.oldScaleX?.let { transform.scaleX = it }
            scale?.oldScaleY?.let { transform.scaleY = it }
        }
    }

    fun setFocus(focus: Entity?) {
        this.focus = focus
    }

    fun setViewportHeight(value: Float) {
        this.viewportHeight = value
    }
}