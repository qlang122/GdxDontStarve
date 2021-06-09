package com.qlang.game.demo.config

object AppConfig {
    const val WORLD_DEF_WIDTH = 1920f
    const val WORLD_DEF_HEIGHT = 1080f

    val worldWidth: Float
        get() {
            return when (1) {
                1 -> WORLD_DEF_WIDTH
                else -> WORLD_DEF_WIDTH
            }
        }
    val worldHeight: Float
        get() {
            return when (1) {
                1 -> WORLD_DEF_HEIGHT
                else -> WORLD_DEF_HEIGHT
            }
        }
}