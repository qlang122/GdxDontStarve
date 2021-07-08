package com.qlang.game.demo.actor.hud

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.qlang.game.demo.entity.GameDate
import com.qlang.h2d.extention.spriter.SpriterObjectComponent
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.label.LabelComponent
import games.rednblack.editor.renderer.factory.EntityFactory
import java.util.*

class PlayClockActor {
    var date: GameDate = GameDate()
        set(value) {
            field = value
            update()
        }

    private val dayTintColor = Color.valueOf("#bd9e45f0")
    private val dayTintLightColor = Color.valueOf("#fcd352f0")
    private val eveningTintColor = Color.valueOf("#784640f0")
    private val eveningTintLightColor = Color.valueOf("#a55a54f0")
    private var needUpdateDay = true
    private val daytime: Int = 9
        get() = if (!needUpdateDay) field else when (date.season) {
            GameDate.Season.SUMMER -> 11
            GameDate.Season.AUTUMN -> 9
            GameDate.Season.WINTER -> 6
            else -> 9
        }
    private val evening: Int = 4
        get() = if (!needUpdateDay) field else when (date.season) {
            GameDate.Season.SUMMER -> 3
            GameDate.Season.AUTUMN -> 4
            GameDate.Season.WINTER -> 5
            else -> 5
        }

    private var isOver = false

    private var clockEntity: Entity? = null
    private var textEntity: Entity? = null

    private var spriter: SpriterObjectComponent? = null

    constructor (clock: Entity?, text: Entity?) {
        clockEntity = clock
        textEntity = text

        val component = clock?.getComponent(MainItemComponent::class.java)
        if (component?.entityType == EntityFactory.SPRITER_TYPE) {
            spriter = clock.getComponent(SpriterObjectComponent::class.java)
        }
        spriter?.isTintEnable = false
    }

    fun update(date: Date) {
        this.date.setTime(date)
        update()
    }

    private fun update() {
        val invisable = hashMapOf<String, Boolean>()
        val dayVisable = hashMapOf<String, Boolean>()
        val eveningVisable = hashMapOf<String, Boolean>()
        for (i in 1..16) {
            invisable["clock_wedge_0${if (i < 10) "0$i" else i}"] = false
        }
        spriter?.animation?.makeTimelineVisible(invisable)

        for (i in 0 until daytime) {
            val name = "clock_wedge_0${if ((i + 1) < 10) "0${i + 1}" else (i + 1)}"
            dayVisable[name] = true

            spriter?.animation?.getTimeline(name)?.let {
                spriter?.animation?.tintSpriteTimeline(it, if (i % 2 == 0) dayTintColor else dayTintLightColor)
            }
        }
        spriter?.animation?.makeTimelineVisible(dayVisable)
        for (i in daytime - 1 until (daytime + evening)) {
            val name = "clock_wedge_0${if ((i + 1) < 10) "0${i + 1}" else (i + 1)}"
            eveningVisable[name] = true

            spriter?.animation?.getTimeline(name)?.let {
                spriter?.animation?.tintSpriteTimeline(it, if (i % 2 == 0) eveningTintColor else eveningTintLightColor)
            }
        }
        spriter?.animation?.makeTimelineVisible(eveningVisable)

        val hourDegrees = (date.hour * 15.0f + 15.0f * date.minute / 60) % 360
        spriter?.animation?.getTimeline("clock_hand")?.keys?.get(0)?.`object`?.setAngle(hourDegrees)

        textEntity?.getComponent(MainItemComponent::class.java)?.let {
            if (it.entityType == EntityFactory.LABEL_TYPE) {
                textEntity?.getComponent(LabelComponent::class.java)?.setText("天 ${date.day}")
            }
        }
    }

    fun updateDay() {
        needUpdateDay = true
    }
}