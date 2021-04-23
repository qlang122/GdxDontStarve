package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.res.GameAssetManager
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log

class HomeMenuStage : Stage() {
    private val manager: AssetManager? = GameAssetManager.instance?.mainManager
    private lateinit var list: List<String>

    private val MENU_NAMES = arrayOf("浏览游戏", "创建游戏", "物品收藏", "选项", "模组", "退出")

    init {
        manager?.let { mgr ->
            val style = List.ListStyle().apply {
                font = mgr.get(R.font.font_cn, BitmapFont::class.java)
                val ui = mgr.get(R.image.ui, TextureAtlas::class.java)
                down = TextureRegionDrawable(ui.findRegion("button"))
                selection = TextureRegionDrawable(ui.findRegion("ds_off")).apply {
                    leftWidth += 50f
                    rightWidth += 50f
                    topHeight += 18f
                    bottomHeight += 18f
                }
            }
            list = List<String>(style).apply {
                setItems(*MENU_NAMES)
                setSize(prefWidth, prefHeight)
                setPosition(100f, 100f)
                addListener(MyClickListener())
            }
            Gdx.input.inputProcessor = this
            addActor(list)
        }

    }

    private inner class MyClickListener : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            val index = list.selectedIndex
            Log.e("QL", "----->$index")
        }
    }
}