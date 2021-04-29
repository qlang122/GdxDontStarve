package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.widget.WidgetList

class WorlListSatge : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var recordList: WidgetList<Actor>? = null

    private var itemClickListener: ((pos: Int) -> Unit)? = null

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont11: BitmapFont? = null
    private var bitmapFont13: BitmapFont? = null

    init {
        manager?.let { mgr ->
            bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }?.let {
                bitmapFont11 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.1f) }
                bitmapFont13 = BitmapFont(BitmapFont.BitmapFontData(it.data.fontFile, false),
                        it.regions, true).apply { data?.setScale(1.3f) }
                it
            }
            val uiTexture = mgr.get(R.image.ui, TextureAtlas::class.java)

            recordList = WidgetList<Actor>(ScrollPane.ScrollPaneStyle(), WidgetList.WidgetListStyle().apply {
                selection = TextureRegionDrawable(uiTexture.findRegion("button_long_over")).apply {
                    leftWidth += 10f
                    topHeight += 18f
                    bottomHeight += 18f
                }
            }).apply {
                setSize(400f, Gdx.graphics.height - 340f)
                setPosition(80f, 180f)
                setScrollingDisabled(false, true)
                setSmoothScrolling(true)
            }

            addActor(Label("游戏", Label.LabelStyle(bitmapFont11, null)).apply {
                setPosition(100f, Gdx.graphics.height - 80f)
            })
            addActor(Label("服务器设定", Label.LabelStyle(bitmapFont11, Color.valueOf("#f2f2f2ff"))).apply {
                setPosition(100f, Gdx.graphics.height - 140f)
            })
            addActor(recordList)

            val list = List<String>(List.ListStyle().apply {
                font = bitmapFont
                selection = TextureRegionDrawable(uiTexture.findRegion("button"))
            })
            list.setItems("1111", "2222", "3333", "4444", "5555", "6666", "7777", "8888", "9999", "0000", "98987")
            list.pack()
            addActor(ScrollPane(list).apply {
                addListener(object : ChangeListener() {
                    override fun changed(event: ChangeEvent?, actor: Actor?) {
                        Log.e("QL", "------->>------${list.selectedIndex}")
                    }
                })
                setSize(400f, 340f)
                setPosition(500f, 180f)
                setScrollingDisabled(false, true)
                setSmoothScrolling(true)
            })
        }
    }

    private fun <T : Actor> newListItem(info: WorlInfo, textureAtlas: TextureAtlas?): T? {
        textureAtlas ?: return null
        bitmapFont ?: return null
        bitmapFont11 ?: return null

        val bgImage = TextureRegionDrawable(textureAtlas.findRegion("background"))
        val roleHead = getRoleHead(info.role, textureAtlas) ?: return null

        return Table().apply {
            add(Container(Image(roleHead).apply { setSize(110f, 120f) }).apply {
                background = bgImage;setSize(prefWidth, prefHeight)
            })
            add(Table().apply {
                add(Label("${info.name}", Label.LabelStyle(bitmapFont11, null)))
                row()
                add(Label("${info.days} 天", Label.LabelStyle(bitmapFont, null))).padTop(10f)
                setSize(prefWidth, prefHeight)
                left()
            }).padLeft(20f)
            setFillParent(true)
            setSize(prefWidth, prefHeight)
        } as T
    }

    fun updateRecords(list: kotlin.collections.List<WorlInfo>) {
        val texture = manager?.get(R.image.saveslot_portraits, TextureAtlas::class.java)
        val items = Array<Actor>()
        list.forEach { newListItem<Actor>(it, texture)?.let { act -> items.add(act) } }
        recordList?.content?.setItems(items)
        recordList?.content?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                itemClickListener?.invoke(recordList?.content?.selectedIndex ?: -1)
            }
        })
    }

    fun setOnItemClickListener(lis: (position: Int) -> Unit) {
        itemClickListener = lis
    }

    private fun getRoleHead(role: String?, textureAtlas: TextureAtlas): TextureRegionDrawable? {
        return trycatch {
            when (role) {
                "waxwell" -> TextureRegionDrawable(textureAtlas.findRegion("waxwell"))
                "wilson" -> TextureRegionDrawable(textureAtlas.findRegion("wilson"))
                "wilton" -> TextureRegionDrawable(textureAtlas.findRegion("wilton"))
                "woodie" -> TextureRegionDrawable(textureAtlas.findRegion("woodie"))
                "wolfgang" -> TextureRegionDrawable(textureAtlas.findRegion("wolfgang"))
                "wx78" -> TextureRegionDrawable(textureAtlas.findRegion("wx78"))
                "wathgrithr" -> TextureRegionDrawable(textureAtlas.findRegion("wathgrithr"))
                "wortox" -> TextureRegionDrawable(textureAtlas.findRegion("wortox"))
                "wendy" -> TextureRegionDrawable(textureAtlas.findRegion("wendy"))
                "willow" -> TextureRegionDrawable(textureAtlas.findRegion("willow"))
                "wes" -> TextureRegionDrawable(textureAtlas.findRegion("wes"))
                "webber" -> TextureRegionDrawable(textureAtlas.findRegion("webber"))
                "winnie" -> TextureRegionDrawable(textureAtlas.findRegion("winnie"))
                "wickerbottom" -> TextureRegionDrawable(textureAtlas.findRegion("wickerbottom"))
                else -> TextureRegionDrawable(textureAtlas.findRegion("random"))
            }
        }
    }
}