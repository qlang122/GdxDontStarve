package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.qlang.eventbus.EventBus
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.component.WorldComponent
import com.qlang.game.demo.event.PlayerBodyIndexEvent
import com.qlang.game.demo.ktx.trycatch
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever
import kotlin.math.abs

class PlayerBodyScript : BasicScript {
    private var worldComponent: WorldComponent? = null
    private var playerComponent: PlayerComponent? = null

    private var timer: Thread? = null
    private var isRunning = false

    private var rootEntity: Entity? = null

    private var oldTime: Long = 0

    constructor() {

    }

    override fun init(item: Entity?) {
        super.init(item)
        item?.let {
            playerComponent = ComponentRetriever.get(it, PlayerComponent::class.java)
        }

        timer = TimerThread()
        timer?.start()
    }

    private fun handleData() {
        playerComponent?.let { player ->
            worldComponent?.let {
                val time = it.gameDate.getTime()
                val b = (time - oldTime) >= 14.4//14.4分钟减一
                player.body.currHunger -= if (b) 1 else 0
                if (b) oldTime = time

            }

            EventBus.post(PlayerBodyIndexEvent.apply {
                health = (player.body.currHealth / player.body.HEALTH).toFloat()
                hunger = (player.body.currHunger / player.body.HUNGER).toFloat()
                sanity = (player.body.currSanity / player.body.SANTITY).toFloat()
                wet = (player.body.currWet / player.body.WET).toFloat()
            })
        }

        trycatch { Thread.sleep(1000) }
    }

    fun setRootEntity(entity: Entity?) {
        rootEntity = entity
        worldComponent = entity?.let { ComponentRetriever.get(it, WorldComponent::class.java) }
    }

    override fun act(delta: Float) {
    }

    override fun dispose() {
        isRunning = false
        timer?.trycatch { interrupt() }
        timer = null
    }

    private inner class TimerThread : Thread("PlayerTimer") {
        override fun run() {
            isRunning = true
            while (isRunning) {
                if (interrupted()) break
                handleData()
            }
            isRunning = false
        }
    }
}