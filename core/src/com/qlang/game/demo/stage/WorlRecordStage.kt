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
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.HorizontalList
import com.qlang.game.demo.widget.VerticalWidgetList

class WorlRecordStage<T> : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private var recordList: VerticalWidgetList<Actor>? = null
    private val infoList = ArrayList<WorlInfo>()
    private var currInfo: WorlInfo? = null

    private var itemClickListener: ((index: Int, t: T?) -> Unit)? = null
    private var itemDeleteListener: ((index: Int, t: T?) -> Unit)? = null
    private var itemPlayListener: ((index: Int, t: T?) -> Unit)? = null
    private var backClickListener: (() -> Unit)? = null

    private var bitmapFont: BitmapFont? = null
    private var bitmapFont11: BitmapFont? = null
    private var bitmapFont13: BitmapFont? = null

    private val paramsTitles = arrayOf("设置", "森林", "洞穴", "模组", "回滚")

    private var currParamsStage: Stage? = null
    private var setStage: ParamsSettingStage? = null
    private var setStyleStage: SettingGameStyleStage? = null

    private var btnDelete: TextButton? = null
    private var btnPlay: TextButton? = null

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

            addActor(Group().apply {
                val panelWidth = Gdx.graphics.width - 480f - 60f + 10f
                val panelHeight = Gdx.graphics.height - 40f - 80f + 60f
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("panel_bg_c")))).apply {
                    width = 150f
                    setPosition(panelWidth / 2f - width / 2f, 10f)
                }
                addActor(Image(NinePatchDrawable(NinePatch(hudTexture.findRegion("panel_bg"), 70, 70, 60, 60))).apply {
                    setSize(panelWidth, panelHeight - 120f)
                })
                setSize(panelWidth, panelHeight)
                setPosition(480f, 40f + 80f - 20f)
            })

            addActor(tabList ?: HorizontalList<Label>(HorizontalList.ListStyle().apply {
                selection = TextureRegionDrawable(hudTexture.findRegion("tab_sl"))
                up = TextureRegionDrawable(hudTexture.findRegion("tab_n"))
                over = TextureRegionDrawable(hudTexture.findRegion("tab_over"))
                rightOffset = 15f
            }).apply {
                tabList = this
                val groupWidth = Gdx.graphics.width - 710f//left 480+20+60, right 80+40
                setPosition(480f + 20f + 60f, Gdx.graphics.height - 120f, Align.left)
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
                addActor(btnDelete ?: TextButton("删除服务器", TextButton.TextButtonStyle().apply {
                    font = bitmapFont
                    up = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                    down = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                    over = TextureRegionDrawable(hudTexture.findRegion("button_over"))
                    disabled = TextureRegionDrawable(hudTexture.findRegion("button_dis"))
                }).apply {
                    isDisabled = true
                    setSize(380f, 90f)
                    addListener(object : ClickListener() {
                        override fun clicked(event: InputEvent?, x: Float, y: Float) {
                            val i = recordList?.content?.selectedIndex ?: -1
                            itemDeleteListener?.invoke(i, currInfo as? T?)
                        }
                    })
                }.also { btnDelete = it })
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("delete"))).apply {
                    setPosition(50f, 25f)
                    setSize(40f, 40f)
                })
                setPosition(480f + 60f + 20f, 40f)//margin left 30, margin list 20
            })

            addActor(btnPlay ?: TextButton("创建世界", TextButton.TextButtonStyle().apply {
                font = bitmapFont
                up = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                down = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                over = TextureRegionDrawable(hudTexture.findRegion("button_over"))
                disabled = TextureRegionDrawable(hudTexture.findRegion("button_dis"))
            }).apply {
                setSize(380f, 90f)
                setPosition(Gdx.graphics.width - 80f - 380f - 60f, 40f)//margin right 80, width 280, margin right 30.
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        val i = recordList?.content?.selectedIndex ?: -1
                        itemPlayListener?.invoke(i, currInfo as? T?)
                    }
                })
            }.also { btnPlay = it })

            setStage = ParamsSettingStage().apply {
                GameManager.instance?.addInputProcessor(this)
            }
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
                add(Label("${info.name}", Label.LabelStyle(bitmapFont11, null)))
                row()
                add(Label(if (info.days <= 0) "" else "${info.days}天", Label.LabelStyle(bitmapFont, null)))
                setSize(prefWidth, prefHeight)
                left()
            }).padLeft(20f)
            setSize(prefWidth, prefHeight)
        } as T
    }

    fun updateRecords(list: kotlin.collections.List<WorlInfo>) {
        val texture = manager?.get(R.image.saveslot_portraits, TextureAtlas::class.java)
        val items = Array<Actor>()
        infoList.clear()
        list.forEach {
            newListItem<Actor>(it, texture)?.let { act ->
                items.add(act);infoList.add(it)
            }
        }
        recordList?.content?.setItems(items)
        recordList?.content?.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val i = recordList?.content?.selectedIndex ?: -1
                currInfo = if (i >= 0 && i < infoList.size) infoList[i] else null
                val b = currInfo?.days ?: 0 <= 0
                setStage?.isEditEnable = b
                if (b) {
                    btnDelete?.isDisabled = true
                    btnPlay?.setText("创建世界")
                } else {
                    btnDelete?.isDisabled = false
                    btnPlay?.setText("回到世界")
                }
                itemClickListener?.invoke(i, currInfo as? T?)
            }
        })
        if (infoList.size > 0 && infoList[0].days > 0) {
            btnDelete?.isDisabled = false
            btnPlay?.setText("回到世界")
        }
    }

    fun setOnItemClickListener(lis: (index: Int, t: T?) -> Unit) {
        itemClickListener = lis
    }

    fun setOnItemDeleteListener(lis: (index: Int, t: T?) -> Unit) {
        itemDeleteListener = lis
    }

    fun setOnItemPlayListener(lis: (index: Int, t: T?) -> Unit) {
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

    override fun dispose() {
        setStage?.let { GameManager.instance?.removeInputProcessor(it) }
        super.dispose()
    }

    private inner class ParamsSettingStage : Stage() {
        private var etName: TextField? = null
        private var etDesc: TextField? = null
        private var etPwd: TextField? = null

        private var btnStyle: TextButton? = null

        var isEditEnable = true

        var gameStyle: Int = 0
            private set

        init {
            manager?.let { mgr ->
                val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)

                addActor(Table().apply {
                    add(Label("游戏风格： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(btnStyle ?: TextButton("社交", TextButton.TextButtonStyle().apply {
                        font = bitmapFont
                        up = TextureRegionDrawable(hudTexture.findRegion("button_n"))
                        disabled = TextureRegionDrawable(hudTexture.findRegion("button_dis"))
                        over = TextureRegionDrawable(hudTexture.findRegion("button_over"))
                    }).apply {
                        setOnClickListener {
                            setStyleStage = SettingGameStyleStage().apply {
                                setOnItemClickListener {
                                    gameStyle = it
                                    btnStyle?.setText(when (it) {
                                        1 -> "合作";2 -> "竞争";3 -> "疯狂";else -> "社交"
                                    })
                                    currParamsStage = setStage
                                    setStyleStage?.let { GameManager.instance?.removeInputProcessor(it) }
                                    setStyleStage = null
                                }
                                GameManager.instance?.addInputProcessor(this)
                            }
                            currParamsStage = setStyleStage
                        }
                    }.also { btnStyle = it }).width(400f)
                    row()
                    add(Label("名称： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(etName ?: TextField("", TextField.TextFieldStyle().apply {
                        font = bitmapFont
                        fontColor = Color.BLACK
                        background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                        focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                    }).also { etName = it }).width(700f).padTop(25f)
                    row()
                    add(Label("描述： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(etDesc ?: TextField("", TextField.TextFieldStyle().apply {
                        font = bitmapFont
                        fontColor = Color.BLACK
                        background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                        focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                    }).also { etDesc = it }).width(700f).padTop(25f)
                    row()
                    add(Label("游戏模式： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(HorizontalGroup().apply {
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f).padLeft(0f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { clearChildren();add(label).padLeft(80f).padRight(80f).size(80f, 60f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f) })
                    }).padTop(25f)
                    row()
                    add(Label("玩家对战： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(HorizontalGroup().apply {
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f).padLeft(0f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { clearChildren();add(label).pad(0f, 80f, 0f, 80f).size(80f, 60f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f) })
                    }).padTop(25f)
                    row()
                    add(Label("玩家： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(HorizontalGroup().apply {
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f).padLeft(0f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { clearChildren();add(label).pad(0f, 80f, 0f, 80f).size(80f, 60f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f) })
                    }).padTop(25f)
                    row()
                    add(Label("密码： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(etPwd ?: TextField("", TextField.TextFieldStyle().apply {
                        font = bitmapFont
                        fontColor = Color.BLACK
                        background = TextureRegionDrawable(hudTexture.findRegion("edit_bg_n"))
                        focusedBackground = TextureRegionDrawable(hudTexture.findRegion("edit_bg_over"))
                    }).also { etPwd = it }).padTop(25f).width(700f)
                    row()
                    add(Label("服务器模式： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(HorizontalGroup().apply {
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f).padLeft(0f) })
                        addActor(TextButton("XXX", TextButton.TextButtonStyle().apply {
                            font = bitmapFont
                            disabledFontColor = Color.DARK_GRAY
                            fontColor = Color.valueOf("#ceab8dff")
                        }).apply { clearChildren();add(label).pad(0f, 80f, 0f, 80f).size(80f, 60f) })
                        addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                            up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                            disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                        }).apply { clearChildren();add(image).size(60f, 60f) })
                    }).padTop(25f)
                    top()
                    setSize(Gdx.graphics.width - 480f - 20f - 60f, Gdx.graphics.height - 40f - 80f - 160f + 30f)
                    setPosition(480f + 20f, 40f + 80f - 20f)
                })
            }
        }
    }

    private inner class SettingGameStyleStage : Stage() {
        private var listener: ((index: Int) -> Unit)? = null

        init {
            manager?.let { mgr ->
                val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)
                val imgTexture = mgr.get(R.image.server_intentions, TextureAtlas::class.java)

                addActor(Table().apply {
                    val panelWidth = Gdx.graphics.width - 480f - 20f - 60f
                    var btnWidth = (panelWidth - 200f) / 4f
                    add(Label("你的服务器是什么游戏风格？", Label.LabelStyle(bitmapFont13, Color.valueOf("#efefefff")))).center().padTop(60f)
                    row()
                    add(Table().apply {
                        add(ImageTextButton("社交", ImageTextButton.ImageTextButtonStyle().apply {
                            font = bitmapFont13;fontColor = Color.BLACK
//                            background = TextureRegionDrawable(hudTexture.findRegion("intentions_bg"))
                            up = TextureRegionDrawable(hudTexture.findRegion("intentions_n"))
                            down = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            over = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            imageUp = TextureRegionDrawable(imgTexture.findRegion("social"))
                        }).apply {
                            clearChildren();add(label).padTop(40f).padBottom(20f);
                            row();add(image).pad(0f, 50f, 20f, 50f)
                            setOnClickListener { listener?.invoke(0) }
                        }).size(btnWidth, btnWidth - 20f)
                        add(ImageTextButton("合作", ImageTextButton.ImageTextButtonStyle().apply {
                            font = bitmapFont13;fontColor = Color.BLACK
//                            background = TextureRegionDrawable(hudTexture.findRegion("intentions_bg"))
                            up = TextureRegionDrawable(hudTexture.findRegion("intentions_n"))
                            down = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            over = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            imageUp = TextureRegionDrawable(imgTexture.findRegion("coop"))
                        }).apply {
                            clearChildren();add(label).padTop(40f).padBottom(20f);
                            row();add(image).pad(0f, 50f, 20f, 50f)
                            setOnClickListener { listener?.invoke(1) }
                        }).size(btnWidth, btnWidth - 20f).space(30f)
                        add(ImageTextButton("竞争", ImageTextButton.ImageTextButtonStyle().apply {
                            font = bitmapFont13;fontColor = Color.BLACK
//                            background = TextureRegionDrawable(hudTexture.findRegion("intentions_bg"))
                            up = TextureRegionDrawable(hudTexture.findRegion("intentions_n"))
                            down = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            over = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            imageUp = TextureRegionDrawable(imgTexture.findRegion("competitive"))
                        }).apply {
                            clearChildren();add(label).padTop(40f).padBottom(20f);
                            row();add(image).pad(0f, 50f, 20f, 50f)
                            setOnClickListener { listener?.invoke(2) }
                        }).size(btnWidth, btnWidth - 20f).space(30f)
                        add(ImageTextButton("疯狂", ImageTextButton.ImageTextButtonStyle().apply {
                            font = bitmapFont13;fontColor = Color.BLACK
                            background = TextureRegionDrawable(hudTexture.findRegion("intentions_bg"))
                            up = TextureRegionDrawable(hudTexture.findRegion("intentions_n"))
                            down = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            over = TextureRegionDrawable(hudTexture.findRegion("intentions_sl"))
                            imageUp = TextureRegionDrawable(imgTexture.findRegion("madness"))
                        }).apply {
                            clearChildren();add(label).padTop(40f).padBottom(20f);
                            row();add(image).pad(0f, 50f, 20f, 50f)
                            setOnClickListener { listener?.invoke(3) }
                        }).size(btnWidth, btnWidth - 20f).space(30f)
                    }).padTop(60f)
                    top()
                    setSize(panelWidth, Gdx.graphics.height - 40f - 80f - 160f + 30f)
                    setPosition(480f + 20f, 40f + 80f - 20f)
                })
            }
        }

        fun setOnItemClickListener(lis: (index: Int) -> Unit) {
            listener = lis
        }

    }
}