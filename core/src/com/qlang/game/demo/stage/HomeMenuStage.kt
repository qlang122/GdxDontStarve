package com.qlang.game.demo.stage

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.MyList
import com.qlang.game.demo.GameManager

class HomeMenuStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager
    private lateinit var list: MyList<String>

    private val MENU_NAMES = arrayOf("浏览游戏", "创建游戏", "物品收藏", "选项", "模组", "退出")

    private var itemClickListener: ((pos: Int) -> Unit)? = null

    init {
        manager?.let { mgr ->
            val style = List.ListStyle().apply {
                val f = mgr.get(R.font.font_cn, BitmapFont::class.java)
                font = BitmapFont(BitmapFont.BitmapFontData(f.data.fontFile, false), f.regions, true).apply {
                    data?.setScale(1.3f)
                }
                val hud = mgr.get(R.image.option_hud, TextureAtlas::class.java)
                down = TextureRegionDrawable(hud.findRegion("item_over"))
                over = TextureRegionDrawable(hud.findRegion("item_over"))
                selection = TextureRegionDrawable(hud.findRegion("item_over")).apply {
                    leftWidth += 50f
                    rightWidth += 50f
                    topHeight += 22f
                    bottomHeight += 22f
                }
            }
            list = MyList<String>(style).apply {
                setItems(*MENU_NAMES)
                setSize(prefWidth, prefHeight)
                setPosition(100f, 50f)
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