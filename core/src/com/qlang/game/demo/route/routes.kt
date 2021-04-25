package com.qlang.game.demo.route

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.utils.Array
import com.qlang.game.demo.utils.Log

class Navigator : ApplicationAdapter() {
    private lateinit var game: Game

    companion object {
        var instance: Navigator? = Navigator()

        private val screens: Array<Screen> = Array()

        /**
         * 替换存在的，并且将其后的所有移除，将自身置于栈顶
         */
        fun <T : Screen> pushReplacement(clazz: Class<T>) {
            var value: Screen? = null
            var index = -1
            for ((i, it) in screens.withIndex()) {
                if (clazz.isInstance(it)) {
                    value = it;index = i
                }
            }
            if (value == null) {
                value = clazz.newInstance()
                screens.add(value)
                instance?.game?.screen = value
            } else {
                val size = screens.size
                if (index > 0 && index != size - 1) {
                    for (i in size - 1 downTo index) {
                        screens.removeValue(screens[i].apply { dispose() }, true)
                    }
                }
                instance?.game?.screen = value
            }
        }

        fun <T : Screen> push(value: T) {
            screens.add(value)
            instance?.game?.screen = value
        }

        fun <T : Screen> pop(value: T) {
            var cls: Screen? = null
            var index = 0
            for ((i, it) in screens.withIndex()) {
                if (value == it) {
                    cls = it;index = i;break
                }
            }
            val size = screens.size
            if (index > 0 && index == size - 1) {
                instance?.game?.screen = screens[size - 2]
            }
            cls?.dispose()
            screens.removeValue(cls, false)

            if (screens.size == 0) {
                Gdx.app.exit()
            }
        }
    }

    fun <T : Game> init(game: T) {
        this.game = game
    }

    fun getGame(): Game = this.game

    override fun dispose() {
        for (it in screens) it.dispose()
        screens.clear()

        instance = null
    }
}