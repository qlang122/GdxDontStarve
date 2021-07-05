package com.qlang.game.demo.mvvm

import com.badlogic.gdx.ScreenAdapter
import com.qlang.gdxkt.lifecycle.Lifecycle
import com.qlang.gdxkt.lifecycle.LifecycleOwner
import com.qlang.gdxkt.lifecycle.LifecycleRegistry

open class LifecycleScreenAdapter : ScreenAdapter(), LifecycleOwner {
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun show() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_SHOW)
    }

    override fun hide() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_HIDE)
    }

    override fun resume() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override fun pause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    override fun dispose() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DISPOSE)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}