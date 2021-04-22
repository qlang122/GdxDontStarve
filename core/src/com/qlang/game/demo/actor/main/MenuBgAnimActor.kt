package com.qlang.game.demo.actor.main

import com.badlogic.gdx.Gdx
import com.qlang.game.demo.actor.BaseAnimActor
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.res.R

class MenuBgAnimActor {

    fun createActor(): Array<BaseAnimActor> {
        val index = GameAssetManager.instance?.homeMenuBgIndex ?: 0
        return when (index) {
            1 -> arrayOf(MenuFeastBgActor(), MenuFeastActor())
            2 -> arrayOf(MenuHalloweenActor())
            3 -> arrayOf(MenuLunacyActor())
            else -> arrayOf(MenuBaseActor())
        }
    }

    private class MenuBaseActor : BaseAnimActor(R.anim.menu.base_atlas) {
        private val manager = GameAssetManager.instance?.mainManager

        init {
            manager?.let {
                loadScml(it, R.anim.menu.base)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    position.set(Gdx.graphics.width.minus(480f), Gdx.graphics.height.minus(150f))
//                    setScale(1f)
                }
            }
        }
    }

    private class MenuFeastActor : BaseAnimActor(R.anim.menu.feast_atlas) {
        private val manager = GameAssetManager.instance?.mainManager

        init {
            manager?.let {
                loadScml(it, R.anim.menu.feast)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    position.set(Gdx.graphics.width.div(2f).minus(10f), Gdx.graphics.height.minus(280f))
                    setScale(1.08f)
                }
            }
        }
    }

    private class MenuFeastBgActor : BaseAnimActor(R.anim.menu.feast_bg_atlas) {
        private val manager = GameAssetManager.instance?.mainManager

        init {
            manager?.let {
                loadScml(it, R.anim.menu.feast_bg)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    position.set(Gdx.graphics.width.div(2f).minus(10f), Gdx.graphics.height.minus(280f))
                    setScale(1.08f)
                }
            }
        }
    }

    private class MenuHalloweenActor : BaseAnimActor(R.anim.menu.halloween_atlas) {
        private val manager = GameAssetManager.instance?.mainManager

        init {
            manager?.let {
                loadScml(it, R.anim.menu.halloween)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    position.set(Gdx.graphics.width.div(2f).plus(300f), Gdx.graphics.height.minus(150f))
                    setScale(1.1f)
                }
            }
        }
    }

    private class MenuLunacyActor : BaseAnimActor(R.anim.menu.lunacy_atlas) {
        private val manager = GameAssetManager.instance?.mainManager

        init {
            manager?.let {
                loadScml(it, R.anim.menu.lunacy)
                loadEntity("dst_menu")
                loadAnimation("loop")

                animation?.root?.apply {
                    position.set(Gdx.graphics.width.div(2f), Gdx.graphics.height.minus(240f))
                    setScale(1.1f)
                }
            }
        }
    }
}