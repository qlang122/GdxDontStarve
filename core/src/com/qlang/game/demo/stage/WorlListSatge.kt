package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
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

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont12: BitmapFont? = null
    private var bitmapFont14: BitmapFont? = null

    init {
        manager?.let { mgr ->
            bitmapFont = mgr.trycatch {
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

    private fun <T : Actor> newListItem(info: WorlInfo, textureAtlas: TextureAtlas?): T? {
        textureAtlas ?: return null
        bitmapFont ?: return null
        bitmapFont12 ?: return null

        val bgImage = TextureRegionDrawable(textureAtlas.findRegion("background"))
        val roleHead = getRoleHead(info.role, textureAtlas) ?: return null

        return Table().apply {
            add(Container(Image(roleHead)).apply {
                setSize(120f, 120f)
                setBackground(bgImage)
            })
            add(Table().apply {
                add(Label("${info.name}", Label.LabelStyle(bitmapFont12, null)))
                row()
                add(Label("${info.days} 天", Label.LabelStyle(bitmapFont, null))).padTop(10f)
            }).padLeft(20f)
        } as T
    }

    fun updateRecords(list: List<WorlInfo>) {
        val texture = manager?.get(R.image.saveslot_portraits, TextureAtlas::class.java)
        val items = list.mapNotNullTo(ArrayList<Actor>()) {
            newListItem(it, texture)
        }
        val actors = items.toArray() as kotlin.Array<out Actor>?
        actors?.let { recordList?.content?.setItems(*actors) }
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