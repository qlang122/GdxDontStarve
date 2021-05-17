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
import com.qlang.game.demo.utils.Log
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
    private var forestStage: ParamsForestStage? = null
    private var setStyleStage: SettingGameStyleStage? = null

    private var btnDelete: TextButton? = null
    private var btnPlay: TextButton? = null

    private var tabList: HorizontalList<Label>? = null
    private val tabClickListener = object : ClickListener() {
        override fun clicked(event: InputEvent?, x: Float, y: Float) {
            val index = tabList?.selectedIndex ?: -1
            currParamsStage = when (index) {
                0 -> setStage
                1 -> forestStage
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
                setSize(500f, Gdx.graphics.height - 320f)
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
                add(image.apply { setSize(60f, 60f) });add(label).padLeft(170f)
                setPosition(100f, 30f)
            })

            addActor(Group().apply {
                val panelWidth = Gdx.graphics.width - 580f - 60f + 10f
                val panelHeight = Gdx.graphics.height - 40f - 80f + 60f
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("panel_bg_c"))).apply {
                    setPosition(panelWidth / 2f - width / 2f, 5f)
                })
                addActor(Image(NinePatchDrawable(NinePatch(hudTexture.findRegion("panel_bg"), 70, 70, 60, 60))).apply {
                    setSize(panelWidth, panelHeight - 120f)
                })
                setSize(panelWidth, panelHeight)
                setPosition(580f, 40f + 80f - 20f)
            })

            addActor(tabList ?: HorizontalList<Label>(HorizontalList.ListStyle().apply {
                selection = TextureRegionDrawable(hudTexture.findRegion("tab_sl"))
                up = TextureRegionDrawable(hudTexture.findRegion("tab_n"))
                over = TextureRegionDrawable(hudTexture.findRegion("tab_over"))
                rightOffset = 15f
            }).apply {
                tabList = this
                val groupWidth = Gdx.graphics.width - 810f//left 480+20+60, right 80+40
                setPosition(580f + 20f + 60f, Gdx.graphics.height - 120f, Align.left)
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
                setPosition(580f + 60f + 20f, 40f)//margin left 30, margin list 20
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

            setStage = ParamsSettingStage()
            forestStage = ParamsForestStage()
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
                align(Align.topLeft)
                add(Label("${info.name}", Label.LabelStyle(bitmapFont11, null)))
                row()
                add(Label(if (info.days <= 0) "" else "${info.days}天", Label.LabelStyle(bitmapFont, null))).padTop(5f).align(Align.left)
                setSize(prefWidth, prefHeight)
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
        if (infoList.size > 0) currInfo = infoList[0]
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

    override fun act() {
        super.act()
        currParamsStage?.act()
    }

    override fun draw() {
        super.draw()
        currParamsStage?.draw()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        currParamsStage?.touchDown(screenX, screenY, pointer, button)
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        currParamsStage?.touchDragged(screenX, screenY, pointer)
        return super.touchDragged(screenX, screenY, pointer)
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        currParamsStage?.touchUp(screenX, screenY, pointer, button)
        return super.touchUp(screenX, screenY, pointer, button)
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        currParamsStage?.mouseMoved(screenX, screenY)
        return super.mouseMoved(screenX, screenY)
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        currParamsStage?.scrolled(amountX, amountY)
        return super.scrolled(amountX, amountY)
    }

    override fun keyDown(keyCode: Int): Boolean {
        currParamsStage?.keyDown(keyCode)
        return super.keyDown(keyCode)
    }

    override fun keyUp(keyCode: Int): Boolean {
        currParamsStage?.keyUp(keyCode)
        return super.keyUp(keyCode)
    }

    override fun keyTyped(character: Char): Boolean {
        currParamsStage?.keyTyped(character)
        return super.keyTyped(character)
    }

    override fun dispose() {
        setStage?.dispose()
        forestStage?.dispose()
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
                            setStyleStage = setStyleStage ?: newStyleSatge()
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
                    add(newModelSelectItem(hudTexture, "生存", {

                    }, {

                    })).padTop(25f)
                    row()
                    add(Label("玩家对战： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(newModelSelectItem(hudTexture, "关闭", {

                    }, {

                    })).padTop(25f)
                    row()
                    add(Label("玩家： ", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                    add(newModelSelectItem(hudTexture, "4", {

                    }, {

                    })).padTop(25f)
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
                    add(newModelSelectItem(hudTexture, "线下", {

                    }, {

                    })).padTop(25f)
                    top()
                    setSize(Gdx.graphics.width - 580f - 20f - 60f, Gdx.graphics.height - 40f - 80f - 160f + 30f)
                    setPosition(580f + 20f, 40f + 80f - 20f)
                })
            }

            if (currInfo == null) {
                setStyleStage = setStyleStage ?: newStyleSatge()
                currParamsStage = setStyleStage
            }
        }

        private fun newModelSelectItem(texture: TextureAtlas, text: String,
                                       onPrev: (txtButton: TextButton?) -> Unit,
                                       onNext: (txtButton: TextButton?) -> Unit): Actor {
            var btnTxt: TextButton? = null
            return HorizontalGroup().apply {
                addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                    up = TextureRegionDrawable(texture.findRegion("arrow_l"))
                    down = TextureRegionDrawable(texture.findRegion("arrow_l"))
                    disabled = TextureRegionDrawable(texture.findRegion("arrow_l_dis"))
                }).apply {
                    clearChildren();add(image).size(60f, 60f).padLeft(0f)
                    setOnClickListener { onPrev(btnTxt) }
                })
                addActor(btnTxt ?: TextButton(text, TextButton.TextButtonStyle().apply {
                    font = bitmapFont
                    disabledFontColor = Color.DARK_GRAY
                    fontColor = Color.valueOf("#ceab8dff")
                }).apply {
                    clearChildren()
                    add(label).pad(0f, 80f, 0f, 80f).size(80f, 60f)
                }.also { btnTxt = it })
                addActor(ImageButton(ImageButton.ImageButtonStyle().apply {
                    up = TextureRegionDrawable(texture.findRegion("arrow_r"))
                    down = TextureRegionDrawable(texture.findRegion("arrow_r"))
                    disabled = TextureRegionDrawable(texture.findRegion("arrow_r_dis"))
                }).apply {
                    clearChildren();add(image).size(60f, 60f)
                    setOnClickListener { onNext(btnTxt) }
                })
            }
        }

        private fun newStyleSatge(): SettingGameStyleStage {
            return SettingGameStyleStage().apply {
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
        }

    }

    private inner class SettingGameStyleStage : Stage() {
        private var listener: ((index: Int) -> Unit)? = null

        init {
            manager?.let { mgr ->
                val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)
                val imgTexture = mgr.get(R.image.server_intentions, TextureAtlas::class.java)

                addActor(Table().apply {
                    val panelWidth = Gdx.graphics.width - 580f - 20f - 60f
                    var btnWidth = (panelWidth - 200f) / 4f
                    add(Label("你的服务器是什么游戏风格？", Label.LabelStyle(bitmapFont13, Color.valueOf("#efefefff")))).center().padTop(60f)
                    row()
                    add(Table().apply {
                        add(newStyleItem("社交", hudTexture, imgTexture, "social", 0))
                                .size(btnWidth, btnWidth - 20f)
                        add(newStyleItem("合作", hudTexture, imgTexture, "coop", 1))
                                .size(btnWidth, btnWidth - 20f).space(30f)
                        add(newStyleItem("竞争", hudTexture, imgTexture, "competitive", 2))
                                .size(btnWidth, btnWidth - 20f).space(30f)
                        add(newStyleItem("疯狂", hudTexture, imgTexture, "madness", 3))
                                .size(btnWidth, btnWidth - 20f).space(30f)
                    }).padTop(60f)
                    top()
                    setSize(panelWidth, Gdx.graphics.height - 40f - 80f - 160f + 30f)
                    setPosition(580f + 20f, 40f + 80f - 20f)
                })
            }
        }

        fun setOnItemClickListener(lis: (index: Int) -> Unit) {
            listener = lis
        }

        private fun newStyleItem(text: String, texture: TextureAtlas, imgTexture: TextureAtlas,
                                 iconName: String, index: Int): Actor {
            return ImageTextButton(text, ImageTextButton.ImageTextButtonStyle().apply {
                font = bitmapFont13;fontColor = Color.BLACK
                up = TextureRegionDrawable(texture.findRegion("intentions_n"))
                down = TextureRegionDrawable(texture.findRegion("intentions_sl"))
                over = TextureRegionDrawable(texture.findRegion("intentions_sl"))
                imageUp = TextureRegionDrawable(imgTexture.findRegion(iconName))
            }).apply {
                clearChildren();add(label).padTop(40f).padBottom(20f)
                row();add(image).pad(0f, 50f, 20f, 50f)
                setOnClickListener { listener?.invoke(index) }
            }
        }
    }

    private inner class ParamsForestStage : Stage() {
        var isEditEnable = true

        init {
            manager?.let { mgr ->
                val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)
                val iconTexture = mgr.get(R.image.customisation, TextureAtlas::class.java)

                val panelWidth = Gdx.graphics.width - 580f - 20f - 60f
                val panelHeight = Gdx.graphics.height - 40f - 80f - 130f
                var topHeight = 0f

                addActor(VerticalGroup().apply {
                    addActor(HorizontalGroup().apply {
                        addActor(Label("森林 预设", Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))))
                        addActor(Table().apply {
                            add(ImageButton(ImageButton.ImageButtonStyle().apply {
                                up = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                                down = TextureRegionDrawable(hudTexture.findRegion("arrow_l"))
                                disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_l_dis"))
                            }).apply { clearChildren();add(image).size(60f, 60f) })
                            add(TextButton("默认", TextButton.TextButtonStyle().apply {
                                font = bitmapFont
                                disabledFontColor = Color.DARK_GRAY
                                fontColor = Color.valueOf("#ceab8dff")
                            }).apply { clearChildren();add(label).pad(0f, 80f, 0f, 80f).size(280f, 60f) })
                            add(ImageButton(ImageButton.ImageButtonStyle().apply {
                                up = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                                down = TextureRegionDrawable(hudTexture.findRegion("arrow_r"))
                                disabled = TextureRegionDrawable(hudTexture.findRegion("arrow_r_dis"))
                            }).apply { clearChildren();add(image).size(60f, 60f) })
                            padLeft(600f)
                        })
                        pad(10f, 0f, 30f, 0f)
                        topHeight += prefHeight
                    })
                    addActor(Label("标准《饥荒》体验。", Label.LabelStyle(bitmapFont, null)).apply {
                        pad(0f, 50f, 15f, 50f)
                        topHeight += prefHeight
                    })
//                    addActor(Image(TextureRegionDrawable(hudTexture.findRegion("slider_line"))))

                    addActor(Group().apply {
                        addActor(ScrollPane(Table().apply {
                            newParamsPanelItems(hudTexture, iconTexture).forEach {
                                add(it).padTop(15f);row()
                            }
                        }).apply {
                            setSize(panelWidth - 80f, panelHeight - topHeight - 50f)
                        })
                        setSize(panelWidth - 80f, panelHeight - topHeight - 50f)
                    })
                    padTop(15f)
                    columnAlign(Align.left);expand()
                    setSize(panelWidth, panelHeight)
                    setPosition(580f + 20f, 40f + 80f)
                })


            }
        }

        private fun newParamsPanelItems(hudTexture: TextureAtlas, iconTexture: TextureAtlas): Array<Actor> {
            var itemW = 0f
            return Array<Actor>().apply {
                val table = Table().apply {
                    add(newSelectItem("创世界：生物群落", "默认", hudTexture,
                            iconTexture, "world_map", {

                    }, {

                    }))
                    add(newSelectItem("创世界：出生点", "默认", hudTexture,
                            iconTexture, "world_start", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight);itemW = prefWidth
                }
                add(TextButton("森林 世界", TextButton.TextButtonStyle(null, null, null, bitmapFont)).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(table)
                add(Table().apply {
                    add(newSelectItem("创世界：大小", "大", hudTexture,
                            iconTexture, "world_size", {

                    }, {

                    }))
                    add(newSelectItem("创世界：分支", "默认", hudTexture,
                            iconTexture, "world_branching", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("创世界：循环", "默认", hudTexture,
                            iconTexture, "world_loop", {

                    }, {

                    }))
                    add(newSelectItem("事件", "默认", hudTexture,
                            iconTexture, "events", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("春", "默认", hudTexture,
                            iconTexture, "spring", {

                    }, {

                    }))
                    add(newSelectItem("夏", "默认", hudTexture,
                            iconTexture, "summer", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })

                add(Table().apply {
                    add(newSelectItem("秋", "默认", hudTexture,
                            iconTexture, "autumn", {

                    }, {

                    }))
                    add(newSelectItem("冬", "默认", hudTexture,
                            iconTexture, "winter", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("起始季节", "秋", hudTexture,
                            iconTexture, "season_start", {

                    }, {

                    }))
                    add(newSelectItem("昼夜选项", "默认", hudTexture,
                            iconTexture, "day", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("雨", "秋", hudTexture,
                            iconTexture, "rain", {

                    }, {

                    }))
                    add(newSelectItem("闪电", "默认", hudTexture,
                            iconTexture, "lightning", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("青蛙雨", "秋", hudTexture,
                            iconTexture, "frog_rain", {

                    }, {

                    }))
                    add(newSelectItem("野火", "默认", hudTexture,
                            iconTexture, "smoke", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("世界再生", "秋", hudTexture,
                            iconTexture, "regrowth", {

                    }, {

                    }))
                    add(newSelectItem("复活石", "默认", hudTexture,
                            iconTexture, "resurrection", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("失败的幸存者", "秋", hudTexture,
                            iconTexture, "skeletons", {

                    }, {

                    }))
                    add(newSelectItem("疾病", "默认", hudTexture,
                            iconTexture, "berrybush_diseased", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("开始资源多样化", "秋", hudTexture,
                            iconTexture, "start_resource", {

                    }, {

                    }))
                    add(newSelectItem("森林石化", "默认", hudTexture,
                            iconTexture, "petrified_tree", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 资源", TextButton.TextButtonStyle(null, null, null, bitmapFont)).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("花，邪恶花", "秋", hudTexture,
                            iconTexture, "flowers", {

                    }, {

                    }))
                    add(newSelectItem("草", "默认", hudTexture,
                            iconTexture, "grass", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树苗", "秋", hudTexture,
                            iconTexture, "sapling", {

                    }, {

                    }))
                    add(newSelectItem("尖灌木", "默认", hudTexture,
                            iconTexture, "marsh_bush", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("风滚草", "秋", hudTexture,
                            iconTexture, "tumbleweeds", {

                    }, {

                    }))
                    add(newSelectItem("芦苇", "默认", hudTexture,
                            iconTexture, "blank_grassy", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树（所有）", "秋", hudTexture,
                            iconTexture, "trees", {

                    }, {

                    }))
                    add(newSelectItem("燧石", "默认", hudTexture,
                            iconTexture, "flint", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("巨石", "秋", hudTexture,
                            iconTexture, "rock", {

                    }, {

                    }))
                    add(newSelectItem("小冰川", "默认", hudTexture,
                            iconTexture, "iceboulder", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("流星区域", "秋", hudTexture,
                            iconTexture, "burntground", {

                    }, {

                    }))
                    add(newSelectItem("流星频率", "默认", hudTexture,
                            iconTexture, "meteor", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 食物", TextButton.TextButtonStyle(null, null, null, bitmapFont)).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("浆果灌木", "秋", hudTexture,
                            iconTexture, "berrybush", {

                    }, {

                    }))
                    add(newSelectItem("胡萝卜", "默认", hudTexture,
                            iconTexture, "carrot", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蘑菇", "秋", hudTexture,
                            iconTexture, "mushrooms", {

                    }, {

                    }))
                    add(newSelectItem("仙人掌", "默认", hudTexture,
                            iconTexture, "cactus", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 动物", TextButton.TextButtonStyle(null, null, null, bitmapFont)).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("兔子", "秋", hudTexture,
                            iconTexture, "rabbits", {

                    }, {

                    }))
                    add(newSelectItem("鼹鼠", "默认", hudTexture,
                            iconTexture, "mole", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蝴蝶", "秋", hudTexture,
                            iconTexture, "butterfly", {

                    }, {

                    }))
                    add(newSelectItem("鸟", "默认", hudTexture,
                            iconTexture, "birds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("美洲鹫", "秋", hudTexture,
                            iconTexture, "buzzard", {

                    }, {

                    }))
                    add(newSelectItem("浣猫", "默认", hudTexture,
                            iconTexture, "catcoon", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("雄火鸡", "秋", hudTexture,
                            iconTexture, "perd", {

                    }, {

                    }))
                    add(newSelectItem("猪", "默认", hudTexture,
                            iconTexture, "pigs", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("伏特山羊", "秋", hudTexture,
                            iconTexture, "lightning_goat", {

                    }, {

                    }))
                    add(newSelectItem("牦牛", "默认", hudTexture,
                            iconTexture, "beefalo", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("牦牛交配频率", "秋", hudTexture,
                            iconTexture, "beefaloheat", {

                    }, {

                    }))
                    add(newSelectItem("狩猎", "默认", hudTexture,
                            iconTexture, "tracks", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("追猎惊喜", "秋", hudTexture,
                            iconTexture, "alternatehunt", {

                    }, {

                    }))
                    add(newSelectItem("企鹅", "默认", hudTexture,
                            iconTexture, "pengull", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("池塘", "秋", hudTexture,
                            iconTexture, "ponds", {

                    }, {

                    }))
                    add(newSelectItem("蜜蜂", "默认", hudTexture,
                            iconTexture, "beehive", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("杀人蜂", "秋", hudTexture,
                            iconTexture, "wasphive", {

                    }, {

                    }))
                    add(newSelectItem("高脚鸟", "默认", hudTexture,
                            iconTexture, "tallbirds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 怪物", TextButton.TextButtonStyle(null, null, null, bitmapFont)).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蜘蛛", "秋", hudTexture,
                            iconTexture, "spiders", {

                    }, {

                    }))
                    add(newSelectItem("猎犬袭击", "默认", hudTexture,
                            iconTexture, "hounds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("猎犬丘", "秋", hudTexture,
                            iconTexture, "houndmound", {

                    }, {

                    }))
                    add(newSelectItem("鱼人", "默认", hudTexture,
                            iconTexture, "merms", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("触手", "秋", hudTexture,
                            iconTexture, "tentacles", {

                    }, {

                    }))
                    add(newSelectItem("发条装置", "默认", hudTexture,
                            iconTexture, "chess_monsters", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("食人花", "秋", hudTexture,
                            iconTexture, "lureplant", {

                    }, {

                    }))
                    add(newSelectItem("海象人营地", "默认", hudTexture,
                            iconTexture, "mactusk", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树精守卫", "秋", hudTexture,
                            iconTexture, "liefs", {

                    }, {

                    }))
                    add(newSelectItem("毒山毛榉树", "默认", hudTexture,
                            iconTexture, "deciduouspoison", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("坎普斯", "秋", hudTexture,
                            iconTexture, "krampus", {

                    }, {

                    }))
                    add(newSelectItem("狂暴熊獾", "默认", hudTexture,
                            iconTexture, "bearger", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("独眼巨鹿", "秋", hudTexture,
                            iconTexture, "deerclops", {

                    }, {

                    }))
                    add(newSelectItem("驼鹿/鹅", "默认", hudTexture,
                            iconTexture, "goosemoose", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("龙蝇", "秋", hudTexture,
                            iconTexture, "dragonfly", {

                    }, {

                    }))
                    add(newSelectItem("蚁狮贡品", "默认", hudTexture,
                            iconTexture, "antlion_tribute", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
            }
        }

        private fun newSelectItem(desc: String, value: String, texture: TextureAtlas,
                                  iconTexture: TextureAtlas, iconName: String,
                                  onPrev: (valueLabel: Label?) -> Unit,
                                  onNext: (valueLabel: Label?) -> Unit): Actor {
            var valLabel: Label? = null
            return Button(Button.ButtonStyle().apply {
                over = TextureRegionDrawable(texture.findRegion("option_over"))
            }).apply {
                clearChildren()
                add(Image(TextureRegionDrawable(iconTexture.findRegion(iconName)))).size(100f, 100f)
                add(ImageButton(ImageButton.ImageButtonStyle().apply {
                    up = TextureRegionDrawable(texture.findRegion("arrow_l"))
                    down = TextureRegionDrawable(texture.findRegion("arrow_l"))
                    disabled = TextureRegionDrawable(texture.findRegion("arrow_l_dis"))
                    setOnClickListener { onPrev(valLabel) }
                })).size(60f, 90f)
                add(Table().apply {
                    add(Label(desc, Label.LabelStyle(bitmapFont, Color.valueOf("#edededff"))))
                    row()
                    valLabel = valLabel
                            ?: Label(value, Label.LabelStyle(bitmapFont, Color.valueOf("#ceab8dff"))).also { valLabel = it }
                    add(valLabel).padTop(10f)
                    setSize(prefWidth, prefHeight)
                }).size(300f, 60f).padLeft(50f).padRight(50f)
                add(ImageButton(ImageButton.ImageButtonStyle().apply {
                    up = TextureRegionDrawable(texture.findRegion("arrow_r"))
                    down = TextureRegionDrawable(texture.findRegion("arrow_r"))
                    disabled = TextureRegionDrawable(texture.findRegion("arrow_r_dis"))
                    setOnClickListener { onNext(valLabel) }
                })).size(60f, 90f)
                setSize(prefWidth, prefHeight)
            }
        }
    }
}