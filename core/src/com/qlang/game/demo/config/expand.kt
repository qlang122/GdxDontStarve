package com.qlang.game.demo.config

inline fun <T, R> T.trycatch(block: T.() -> R?): R? {
    return try {
        block()
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}