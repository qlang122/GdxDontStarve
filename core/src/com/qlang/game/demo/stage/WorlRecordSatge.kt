package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.HorizontalList
import com.qlang.game.demo.widget.VerticalWidgetList

class WorlRecordSatge : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var recordList: VerticalWidgetList<Actor>? = null

    private var itemClickListener: ((pos: Int) -> Unit)? = null
    private var backClickListener: (() -> Unit)? = null

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont11: BitmapFont? = null
    private var bitmapFont13: BitmapFont? = null

    private val paramsTitles = arrayOf("设置", "森林", "洞穴", "模组", "回滚")

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

            recordList = VerticalWidgetList<Actor>(ScrollPane.ScrollPaneStyle(), VerticalWidgetList.WidgetListStyle().apply {
                selection = TextureRegionDrawable(uiTexture.findRegion("button_long_over")).apply {
                    leftWidth += 10f
                    topHeight += 18f
                    bottomHeight += 18f
                }
            }).apply {
                setSize(400f, Gdx.graphics.height - 320f)
                setPosition(80f, 150f)
            }

            addActor(Label("游戏", Label.LabelStyle(bitmapFont11, null)).apply {
                setPosition(100f, Gdx.graphics.height - 80f)
            })
            addActor(Label("服务器设定", Label.LabelStyle(bitmapFont11, Color.valueOf("#f2f2f2ff"))).apply {
                setPosition(100f, Gdx.graphics.height - 140f)
            })

            addActor(recordList)

            addActor(ImageTextButton("返回", ImageTextButton.ImageTextButtonStyle().apply {
                font = bitmapFont13
                up = TextureRegionDrawable(uiTexture.findRegion("arrow_left"))
                down = TextureRegionDrawable(uiTexture.findRegion("arrow_left_over"))
            }).apply {
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        backClickListener?.invoke()
                    }
                })
                clearChildren()
                add(image);add(label).padLeft(170f)
                setPosition(100f, 30f)
            })

            addActor(Table().apply {
                val groupWidth = Gdx.graphics.width - 640f//left 480+30+20, right 80+30
                paramsTitles.forEach {
                    add(TextButton(it, TextButton.TextButtonStyle().apply {
                        font = bitmapFont11
                        up = TextureRegionDrawable(uiTexture.findRegion("button"))
                        down = TextureRegionDrawable(uiTexture.findRegion("button_over"))
                    })).padLeft(-5f).minWidth(groupWidth / 5f)
                }
                background = TextureRegionDrawable(uiTexture.findRegion("textbox_long"))
                setSize(groupWidth, 80f)
                setPosition(480f + 30f + 20f, Gdx.graphics.height - 160f, Align.left)//margin top 80, height 60
            })
            addActor(HorizontalList<Label>(HorizontalList.ListStyle().apply {
                selection = TextureRegionDrawable(uiTexture.findRegion("button_over"))
                background = TextureRegionDrawable(uiTexture.findRegion("textbox_long"))
            }).apply {
                val groupWidth = Gdx.graphics.width - 640f//left 480+30+20, right 80+30
                setPosition(480f + 30f + 20f, Gdx.graphics.height - 260f, Align.left)
                val items = Array<Label>()
                paramsTitles.forEach {
                    items.add(Label(it, Label.LabelStyle(bitmapFont11, null)).apply {
                        setAlignment(Align.center);setSize(groupWidth / 5f, 60f)
                    })
                }
                setItems(items)
                setSize(prefWidth, prefHeight)
            })

            addActor(Group().apply {
                addActor(TextButton("删除服务器", TextButton.TextButtonStyle().apply {
                    font = bitmapFont
                    up = TextureRegionDrawable(uiTexture.findRegion("button"))
                    down = TextureRegionDrawable(uiTexture.findRegion("button_over"))
                }).apply { setSize(280f, 80f) })
//                addActor(Image(TextureRegionDrawable(uiTexture.findRegion("delete"))))
                setPosition(480f + 30f + 20f, 40f)//margin left 30, margin list 20
            })

            addActor(TextButton("回到世界", TextButton.TextButtonStyle().apply {
                font = bitmapFont
                up = TextureRegionDrawable(uiTexture.findRegion("button"))
                down = TextureRegionDrawable(uiTexture.findRegion("button_over"))
            }).apply {
                setSize(280f, 80f)
                setPosition(Gdx.graphics.width - 80f - 280f - 30f, 40f)//margin right 80, width 280, margin right 30.
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
                left()
                add(Label("${info.name}", Label.LabelStyle(bitmapFont11, null)))
                row()
                add(Label("${info.days} 天", Label.LabelStyle(bitmapFont, null))).padTop(10f)
                setSize(prefWidth, prefHeight)
            }).padLeft(20f)
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

    fun setBackClickListener(lis: () -> Unit) {
        backClickListener = lis
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