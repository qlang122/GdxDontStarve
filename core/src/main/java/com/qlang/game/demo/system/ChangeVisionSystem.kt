package com.qlang.game.demo.system

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.qlang.game.demo.component.ScaleEntityComponent
import games.rednblack.editor.renderer.components.BoundingBoxComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.ZIndexComponent
import kotlin.math.abs

class ChangeVisionSystem : IteratingSystem {
    private val boundingBoxMapper: ComponentMapper<BoundingBoxComponent> = ComponentMapper.getFor(BoundingBoxComponent::class.java)
    private val transformMapper: ComponentMapper<TransformComponent> = ComponentMapper.getFor(TransformComponent::class.java)
    private val mainItemMapper: ComponentMapper<MainItemComponent> = ComponentMapper.getFor(MainItemComponent::class.java)
    private val zIndexMapper: ComponentMapper<ZIndexComponent> = ComponentMapper.getFor(ZIndexComponent::class.java)
    private val scaleEntityMapper: ComponentMapper<ScaleEntityComponent> = ComponentMapper.getFor(ScaleEntityComponent::class.java)

    private var viewOffset = 0.25f
    private var viewportHeight: Float = 0f

    private var focus: Entity? = null
    private val view = Rectangle()

    constructor() : this(0.25f)

    constructor(viewOffset: Float) : super(Family.all(ScaleEntityComponent::class.java).get()) {
        this.viewOffset = viewOffset
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (focus == null) return

        val transform = transformMapper.get(entity)
        val scale = scaleEntityMapper.get(entity)?.apply {
            if (oldScaleX == -1f) oldScaleX = transform.scaleX
            if (oldScaleY == -1f) oldScaleY = transform.scaleY
        }

        val m = mainItemMapper.get(entity)

        if (!m.culled) {
            val pT = transformMapper.get(focus)
            val pZ = zIndexMapper.get(focus)
            val pB = boundingBoxMapper.get(focus)
            val z = zIndexMapper.get(entity)
            val b = boundingBoxMapper.get(entity)

            val fl = transform.y - pT.y
            val s = abs(fl) / viewportHeight / 2f * viewOffset
            val overlaps = pB.rectangle.overlaps(b.rectangle)

            when {
                fl > 0 -> {
                    scale?.oldScaleX?.minus(s)?.let { transform.scaleX = it }
                    scale?.oldScaleY?.minus(s)?.let { transform.scaleY = it }
                    if (overlaps) pZ.zIndex = z.zIndex + 1
                }
                fl < 0 -> {
                    scale?.oldScaleX?.plus(s)?.let { transform.scaleX = it }
                    scale?.oldScaleY?.plus(s)?.let { transform.scaleY = it }
                    if (overlaps) pZ.zIndex = z.zIndex - 1
                }
            }
        } else {
            scale?.oldScaleX?.let { transform.scaleX = it }
            scale?.oldScaleY?.let { transform.scaleY = it }
        }
    }

    fun setPlayer(focus: Entity?) {
        this.focus = focus
    }

    fun setViewportHeight(value: Float) {
        this.viewportHeight = value
    }
}