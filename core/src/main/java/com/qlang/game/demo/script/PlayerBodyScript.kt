package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.qlang.eventbus.EventBus
import com.qlang.game.demo.component.PlayerComponent
import com.qlang.game.demo.component.WorldComponent
import com.qlang.game.demo.event.PlayerBodyIndexEvent
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.utils.Log
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

class PlayerBodyScript : BasicScript {
    private var worldComponent: WorldComponent? = null
    private var playerComponent: PlayerComponent? = null

    private var timer: Thread? = null
    private var isRunning = false

    private var rootEntity: Entity? = null

    private var oldHungerTime: Float = 0f
    private var oldSanityTime: Float = 0f

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
                val time = it.gameDate.getGameTime()
                val b = (time - oldHungerTime) >= 1//10分钟减一
                player.body.currHunger -= if (b) 1 else 0
                if (b) oldHungerTime = time

                val bS = (time - oldSanityTime) >= 2//15分钟减一
                player.body.currSanity -= if (bS) 1 else 0
                if (bS) oldSanityTime = time

            }

            EventBus.post(PlayerBodyIndexEvent.apply {
                health = player.body.currHealth.toFloat() / player.body.HEALTH
                hunger = player.body.currHunger.toFloat() / player.body.HUNGER
                sanity = player.body.currSanity.toFloat() / player.body.SANTITY
                wet = (player.body.currWet / player.body.WET).toFloat()
            })
        }
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
                trycatch { sleep(5000) }
            }
            isRunning = false
        }
    }
}