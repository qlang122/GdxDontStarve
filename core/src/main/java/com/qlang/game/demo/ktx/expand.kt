package com.qlang.game.demo.ktx

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import games.rednblack.editor.renderer.components.additional.ButtonComponent

inline fun <T, R> T.trycatch(block: T.() -> R?): R? {
    return try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

inline fun <T : Actor> T.setOnClickListener(crossinline lis: () -> Unit) {
    this.addListener(object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            lis()
        }
    })
}

inline fun ButtonComponent.setOnClickListener(crossinline lis: () -> Unit) {
    this.addListener(object : ButtonComponent.ButtonListener {
        override fun touchUp() {
        }

        override fun touchDown() {
        }

        override fun clicked() {
            lis()
        }
    })
}