package com.qlang.game.demo.script

import com.badlogic.ashley.core.Entity
import com.qlang.eventbus.EventBus
import com.qlang.game.demo.component.WorldComponent
import com.qlang.game.demo.event.WorldTimeEvent
import com.qlang.game.demo.ktx.trycatch
import games.rednblack.editor.renderer.scripts.BasicScript
import games.rednblack.editor.renderer.utils.ComponentRetriever

class WorldScript : BasicScript {
    private var worldComponent: WorldComponent? = null

    private var timer: Thread? = null
    private var isRunning = false

    constructor() {

    }

    override fun init(item: Entity?) {
        super.init(item)
        item?.let {
            worldComponent = ComponentRetriever.get(it, WorldComponent::class.java)
        }

        timer = TimerThread()
        timer?.start()
    }

    private fun handleData() {
        worldComponent?.let {
            it.time++
            it.gameDate.addTime(2)
            EventBus.post(WorldTimeEvent.apply { date = it.gameDate })
        }
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
                trycatch { sleep(2000) }
            }
            isRunning = false
        }
    }
}