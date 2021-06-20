package com.qlang.game.demo.actor.hud

import com.badlogic.ashley.core.Entity
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.factory.EntityFactory

class PlayerWetMeterActor {
    private var mainEntity: Entity? = null
    private var arrowEntity: Entity? = null
    private var healthSpriter: SpriterObjectComponent? = null
    private var arrowSpriter: SpriterObjectComponent? = null

    private var currStage = BodyIndexState.NEUTRAL

    private var currProgress = 1f

    constructor(main: Entity?, arrow: Entity?) {
        mainEntity = main
        arrowEntity = arrow

        val component = mainEntity?.getComponent(MainItemComponent::class.java)
        if (component?.entityType == EntityFactory.SPRITER_TYPE) {
            healthSpriter = mainEntity?.getComponent(SpriterObjectComponent::class.java)
        }

        val arrowComponent = arrow?.getComponent(MainItemComponent::class.java)
        if (arrowComponent?.entityType == EntityFactory.SPRITER_TYPE) {
            arrowSpriter = arrow.getComponent(SpriterObjectComponent::class.java)
        }
    }

    /**
     * @param value the value must be in 0..1
     */
    fun setProgress(value: Float) {
        if (value < 0 || value >= 1) return
        val len = healthSpriter?.animation?.length ?: 0
        currProgress = value * len
        healthSpriter?.animation?.apply {
            setPlay(true);update(currProgress);setPlay(false)
        }
    }

    fun changeState(value: BodyIndexState = BodyIndexState.NEUTRAL) {
        currStage = value
        val arrowAnim = when (currStage) {
            BodyIndexState.DESC -> "arrow_loop_decrease_90s"
            BodyIndexState.DESC_MORE -> "arrow_loop_decrease_more_90s"
            BodyIndexState.DESC_MOST -> "arrow_loop_decrease_most_90s"
            BodyIndexState.INC -> "arrow_loop_increase_90s"
            BodyIndexState.INC_MORE -> "arrow_loop_increase_more_90s"
            BodyIndexState.INC_MOST -> "arrow_loop_increase_most_90s"
            else -> "neutral_90s"
        }
        arrowSpriter?.setAnimation(arrowAnim)
    }

    fun act(delta: Float) {
        arrowSpriter?.animation?.let {
            if (it.isDone) it.reset()
            it.update(delta.times(1000))//delta单位为s，0.001
        }
    }
}