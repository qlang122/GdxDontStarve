package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.WidgetList

class WorlListSatge : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var recordList: WidgetList<Actor>? = null

    private var itemClickListener: ((pos: Int) -> Unit)? = null

    init {
        manager?.let { mgr ->
            var bitmapFont12: BitmapFont? = null
            var bitmapFont14: BitmapFont? = null
            val bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }?.let {
                bitmapFont12 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.2f) }
                bitmapFont14 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.4f) }
                it
            }
            val uiTexture = mgr.get(R.image.ui, TextureAtlas::class.java)

            recordList = WidgetList<Actor>(ScrollPane.ScrollPaneStyle(), WidgetList.WidgetListStyle().apply {
                selection = TextureRegionDrawable(uiTexture.findRegion("button_long_over")).apply {
                    leftWidth += 50f
                    rightWidth += 50f
                    topHeight += 18f
                    bottomHeight += 18f
                }
            }).apply {
                setSize(400f, Gdx.graphics.height - 350f)
                setPosition(100f, Gdx.graphics.height - 140f)
                setScrollingDisabled(false, true)
                setSmoothScrolling(true)
            }

            addActor(Label("游戏", Label.LabelStyle(bitmapFont12, null)).apply {
                setPosition(100f, Gdx.graphics.height - 60f)
            })
            addActor(Label("服务器设定", Label.LabelStyle(bitmapFont14, Color.valueOf("#d0d0d0ff"))).apply {
                setPosition(100f, Gdx.graphics.height - 100f)
            })
            addActor(recordList)
        }
    }

    private fun <T : Actor> newListItem(info: WorlInfo): T {

        return Actor() as T
    }

    fun updateRecords(list: List<WorlInfo>) {
        val items = list.mapTo(ArrayList<Actor>()) {
            newListItem(it)
        }
        val actors = items.toArray() as kotlin.Array<out Actor>?
        recordList?.content?.setItems(*actors!!)
    }

    fun setOnItemClickListener(lis: (position: Int) -> Unit) {
        itemClickListener = lis
    }
}