package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FillViewport
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.MyList
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.config.AppConfig

class HomeMenuStage : Stage(FillViewport(AppConfig.worldWidth, AppConfig.worldHeight)) {
    private val manager: AssetManager? = GameManager.instance?.mainManager
    private lateinit var list: MyList<String>

    private val MENU_NAMES = arrayOf("浏览游戏", "创建游戏", "物品收藏", "选项", "模组", "退出")

    private var itemClickListener: ((pos: Int) -> Unit)? = null

    init {
        manager?.let { mgr ->
            val skin = mgr.get(R.skin.option_hud, Skin::class.java)
            list = MyList<String>(skin, "font22").apply {
                this.style?.selection = skin.newDrawable(this.style.selection)?.apply {
                    leftWidth += 50f;rightWidth += 50f;topHeight += 22f;bottomHeight += 22f
                }
                setItems(*MENU_NAMES)
                setSize(400f, prefHeight)
                setPosition(80f, 50f)
                addListener(MyClickListener())
            }
            addActor(list)
        }
    }

    fun setOnItemClickListener(lis: (position: Int) -> Unit) {
        itemClickListener = lis
    }

    private inner class MyClickListener : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            itemClickListener?.invoke(list.selectedIndex)
        }
    }
}