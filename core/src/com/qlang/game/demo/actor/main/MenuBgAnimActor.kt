package com.qlang.game.demo.actor.main

import com.badlogic.gdx.Gdx
import com.qlang.game.demo.actor.BaseAnimActor
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.res.R

class MenuBgAnimActor {

    fun createActor(): Array<BaseAnimActor> {
        val index = GameManager.instance?.homeMenuBgIndex ?: 0
        return when (index) {
            1 -> arrayOf(MenuFeastBgActor(), MenuFeastActor())
            2 -> arrayOf(MenuHalloweenActor())
            3 -> arrayOf(MenuLunacyActor())
            else -> arrayOf(MenuBaseActor())
        }
    }

    private class MenuBaseActor : BaseAnimActor(R.anim.menu.base_atlas) {
        private val manager = GameManager.instance?.mainManager

        init {
            //背景大小1944*716

            val width = Gdx.graphics.width
            val height = Gdx.graphics.height

            manager?.let {
                loadScml(it, R.anim.menu.base)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    val scale = width.div(1934f)//1944-10图片有白边
                    position.set(10f, height.minus(356f.div(scale).plus(100f)))//356=716/2
                    setScale(scale)
                }
            }
        }
    }

    private class MenuFeastActor : BaseAnimActor(R.anim.menu.feast_atlas) {
        private val manager = GameManager.instance?.mainManager

        init {
            //背景大小1944*684

            val width = Gdx.graphics.width
            val height = Gdx.graphics.height

            manager?.let {
                loadScml(it, R.anim.menu.feast)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    val scale = width.div(1904f)
                    position.set(width.div(2f), height.minus(342f.div(scale).minus(20f)))//684/2图片高的一半
                    setScale(scale)
                }
            }
        }
    }

    private class MenuFeastBgActor : BaseAnimActor(R.anim.menu.feast_bg_atlas) {
        private val manager = GameManager.instance?.mainManager

        init {
            //背景大小1944*684

            val width = Gdx.graphics.width
            val height = Gdx.graphics.height

            manager?.let {
                loadScml(it, R.anim.menu.feast_bg)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    val scale = width.div(1904f)
                    position.set(width.div(2f), height.minus(342f.div(scale).minus(20f)))//684/2图片高的一半
                    setScale(scale)
                }
            }
        }
    }

    private class MenuHalloweenActor : BaseAnimActor(R.anim.menu.halloween_atlas) {
        private val manager = GameManager.instance?.mainManager

        init {
            //背景大小1932*564

            val width = Gdx.graphics.width
            val height = Gdx.graphics.height

            manager?.let {
                loadScml(it, R.anim.menu.halloween)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    val scale = width.div(1912f)
                    position.set(-10f, height.div(2).minus(20f))//564/2图片高的一半
                    setScale(scale)
                }
            }
        }
    }

    private class MenuLunacyActor : BaseAnimActor(R.anim.menu.lunacy_atlas) {
        private val manager = GameManager.instance?.mainManager

        init {
            //背景大小1933*552

            val width = Gdx.graphics.width
            val height = Gdx.graphics.height

            manager?.let {
                loadScml(it, R.anim.menu.lunacy)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    val scale = width.div(1914f)
                    position.set(width.div(2f).plus(5f), height.minus(276f.div(scale)))//552/2图片高的一半
                    setScale(scale)
                }
            }
        }
    }
}