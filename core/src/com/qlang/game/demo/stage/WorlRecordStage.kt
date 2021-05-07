package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable
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

class WorlRecordStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var recordList: VerticalWidgetList<Actor>? = null

    private var itemClickListener: ((index: Int) -> Unit)? = null
    private var itemDeleteListener: ((index: Int) -> Unit)? = null
    private var itemPlayListener: ((index: Int) -> Unit)? = null
    private var backClickListener: (() -> Unit)? = null

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont11: BitmapFont? = null
    private var bitmapFont13: BitmapFont? = null

    private val paramsTitles = arrayOf("设置", "森林", "洞穴", "模组", "回滚")

    private var currParamsStage: Stage? = null
    private var setStage: ParamsSettingStage? = null

    private var tabList: HorizontalList<Label>? = null
    private val tabClickListener = object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            val index = tabList?.selectedIndex ?: -1
            currParamsStage = when (index) {
                0 -> setStage
                else -> null
            }
        }
    }

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
            val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)

            recordList = VerticalWidgetList<Actor>(ScrollPane.ScrollPaneStyle(), VerticalWidgetList.WidgetListStyle().apply {
                selection = TextureRegionDrawable(hudTexture.findRegion("item_select")).apply {
                    leftWidth += 20f
                    topHeight += 15f
                    bottomHeight += 15f
                }
                down = TextureRegionDrawable(hudTexture.findRegion("item_over"))
                over = TextureRegionDrawable(hudTexture.findRegion("item_over"))
            }).apply {
                setSize(400f, Gdx.graphics.height - 320f)
                setPosition(80f, 150f)
            }

            addActor(Image(TextureRegionDrawable(mgr.get(R.image.bg_redux_wardrobe_bg,
                    TextureAtlas::class.java).findRegion("wardrobe_bg"))).apply {
                setSize(Gdx.graphics.width.plus(300f), Gdx.graphics.height.plus(300f))
                setPosition(-150f, -150f)
            })

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

            addActor(tabList ?: HorizontalList<Label>(HorizontalList.ListStyle().apply {
                selection = TextureRegionDrawable(hudTexture.findRegion("tab_sl")).apply {
                    leftWidth -= 5f;rightWidth -= 5f
                }
                up = TextureRegionDrawable(hudTexture.findRegion("tab_n")).apply {
                    leftWidth -= 5f;rightWidth -= 5f
                }
                over = TextureRegionDrawable(hudTexture.findRegion("tab_over")).apply {
                    leftWidth -= 5f;rightWidth -= 5f
                }
            }).apply {
                tabList = this
                val groupWidth = Gdx.graphics.width - 660f//left 480+20+40, right 80+40
                setPosition(480f + 20f + 40f, Gdx.graphics.height - 160f, Align.left)
                val items = Array<Label>()
                paramsTitles.forEach {
                    items.add(Label(it, Label.LabelStyle(bitmapFont11, null)).apply {
                        setAlignment(Align.center);setSize(groupWidth / 5f, 60f)
                    })
                }
                setItems(items)
                setSize(prefWidth, prefHeight)
                addListener(tabClickListener)
            })

            addActor(Group().apply {
                val panelWidth = Gdx.graphics.width - 480f - 80f + 10f
                val panelHeight = Gdx.graphics.height - 40f - 80f + 20f
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("panel_bg_c")))).apply {
                    setPosition(panelWidth / 2f - panelWidth / 2f, 10f)
                }
                addActor(Image(NinePatchDrawable(NinePatch(hudTexture.findRegion("panel_bg"), 70, 70, 60, 60))).apply {
                    setSize(panelWidth - 20, panelHeight - 120f)
                })
                setSize(panelWidth, panelHeight)
                setPosition(480f, 40f + 80f - 20f)
            })

            addActor(Group().apply {
                addActor(TextButton("删除服务器", TextButton.TextButtonStyle().apply {
                    font = bitmapFont
                    up = TextureRegionDrawable(uiTexture.findRegion("button"))
                    down = TextureRegionDrawable(uiTexture.findRegion("button_over"))
                }).apply {
                    setSize(280f, 80f)
                    addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            itemDeleteListener?.invoke(recordList?.content?.selectedIndex ?: -1)
                        }
                    })
                })
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("delete"))))
                setPosition(480f + 30f + 20f, 40f)//margin left 30, margin list 20
            })

            addActor(TextButton("回到世界", TextButton.TextButtonStyle().apply {
                font = bitmapFont
                up = TextureRegionDrawable(uiTexture.findRegion("button"))
                down = TextureRegionDrawable(uiTexture.findRegion("button_over"))
            }).apply {
                setSize(280f, 80f)
                setPosition(Gdx.graphics.width - 80f - 280f - 30f, 40f)//margin right 80, width 280, margin right 30.
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        itemPlayListener?.invoke(recordList?.content?.selectedIndex ?: -1)
                    }
                })
            })

            setStage = ParamsSettingStage()
            currParamsStage = setStage
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

    fun setOnItemClickListener(lis: (index: Int) -> Unit) {
        itemClickListener = lis
    }

    fun setOnItemDeleteListener(lis: (index: Int) -> Unit) {
        itemDeleteListener = lis
    }

    fun setOnItemPlayListener(lis: (index: Int) -> Unit) {
        itemPlayListener = lis
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

    override fun draw() {
        super.draw()
        currParamsStage?.draw()
    }

    private inner class ParamsSettingStage : Stage() {
        private var etName: TextField? = null
        private var etDesc: TextField? = null
        private var etPwd: TextField? = null

        init {
            manager?.let { mgr ->
                val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)

                addActor(Table().apply {
                    add(HorizontalGroup().apply {
                        addActor(Label("游戏风格： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            up = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("button_dis"))
                            over = TextureRegionDrawable(hudTexture.findRegion("button_over"))
                        }).apply { setSize(300f, 60f) })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("名称： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(etName ?: TextField("", TextField.TextFieldStyle().apply {
                            font = bitmapFont
                            fontColor = Color.BLACK
                            background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                            focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                        }).apply { setSize(500f, 60f) }.also { etName = it })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("描述： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(etDesc ?: TextField("", TextField.TextFieldStyle().apply {
                            font = bitmapFont
                            fontColor = Color.BLACK
                            background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                            focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                        }).apply { setSize(500f, 60f) }.also { etDesc = it })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("游戏模式： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { setSize(60f, 80f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { setSize(80f, 80f);padLeft(40f);padRight(40f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { setSize(60f, 80f) })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("游戏对战： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { setSize(60f, 80f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { setSize(80f, 80f);padLeft(40f);padRight(40f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { setSize(60f, 80f) })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("玩家： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { setSize(60f, 80f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { setSize(80f, 80f);padLeft(40f);padRight(40f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { setSize(60f, 80f) })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("密码： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(etPwd ?: TextField("", TextField.TextFieldStyle().apply {
                            font = bitmapFont
                            fontColor = Color.BLACK
                            background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                            focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                        }).apply { setSize(500f, 60f) }.also { etPwd = it })
                    });row()
                    add(HorizontalGroup().apply {
                        addActor(Label("服务器模式： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { setSize(60f, 80f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { setSize(80f, 80f);padLeft(40f);padRight(40f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { setSize(60f, 80f) })
                    })

                    setSize(Gdx.graphics.width - 480f - 20f - 80f, Gdx.graphics.height - 40f - 80f - 160f + 20f)
                    setPosition(480f + 20f, 40f + 80f - 20f)
                })
            }
        }
    }
}