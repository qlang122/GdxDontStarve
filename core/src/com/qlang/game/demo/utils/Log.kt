package com.qlang.game.demo.utils

import com.badlogic.gdx.Gdx

object Log {
    @JvmStatic
    fun i(tag: String, vararg values: Any?) {
        val buff = StringBuffer()
        for (value in values) {
            buff.append(value?.toString()).append(" ")
        }
        Gdx.app.log(tag, buff.toString())
    }

    @JvmStatic
    fun d(tag: String, vararg values: Any?) {
        val buff = StringBuffer()
        for (value in values) {
            buff.append(value?.toString()).append(" ")
        }
        Gdx.app.debug(tag, buff.toString())
    }

    @JvmStatic
    fun e(tag: String, vararg values: Any?) {
        val buff = StringBuffer()
        for (value in values) {
            buff.append(value?.toString()).append(" ")
        }
        Gdx.app.error(tag, buff.toString())
    }
}