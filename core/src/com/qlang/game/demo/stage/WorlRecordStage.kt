package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
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
            val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)
            val uiSkin = mgr.get(R.skin.ui, Skin::class.java)
            val hudTexture = mgr.get(R.image.option_hud, TextureAtlas::class.java)

            val listStyle = hudSkin.get(VerticalWidgetList.WidgetListStyle::class.java)
            recordList = VerticalWidgetList<Actor>(ScrollPane.ScrollPaneStyle(), listStyle.apply {
                selection = trycatch { hudSkin.newDrawable(selection) }?.apply { leftWidth += 20f;topHeight += 15f;bottomHeight += 15f }
            }).apply {
                setSize(400f, Gdx.graphics.height - 320f)
                setPosition(80f, 150f)
            }

            addActor(Image(TextureRegionDrawable(mgr.get(R.image.bg_redux_wardrobe_bg,
                    TextureAtlas::class.java).findRegion("wardrobe_bg"))).apply {
                setSize(Gdx.graphics.width.plus(300f), Gdx.graphics.height.plus(300f))
                setPosition(-150f, -150f)
            })

            addActor(Label("游戏", hudSkin, "font16").apply {
                setPosition(100f, Gdx.graphics.height - 80f)
            })
            addActor(Label("服务器设定", hudSkin, "font18-f2f2f2").apply {
                setPosition(100f, Gdx.graphics.height - 140f)
            })

            addActor(recordList)

            addActor(ImageTextButton("返回", uiSkin, "font24").apply {
                style?.up?.minWidth = 0f;style?.up?.minHeight = 0f
                style?.down?.minWidth = 0f;style?.down?.minHeight = 0f
                style?.over?.minWidth = 0f;style?.over?.minHeight = 0f
                setOnClickListener { backClickListener?.invoke() }
                clearChildren()
                add(image).size(60f, 60f);add(label).padLeft(170f)
                setPosition(100f, 30f)
            })

            addActor(Group().apply {
                val panelWidth = Gdx.graphics.width - 480f - 60f + 10f
                val panelHeight = Gdx.graphics.height - 40f - 80f + 60f
                addActor(hudSkin.get("panel-bg-c", Image::class.java).apply {
                    setPosition(panelWidth / 2f - width / 2f, 5f)
                })
                addActor(Image(NinePatchDrawable(NinePatch(hudTexture.findRegion("panel_bg"), 70, 70, 60, 60))).apply {
                    setSize(panelWidth, panelHeight - 120f)
                })
                setSize(panelWidth, panelHeight)
                setPosition(480f, 40f + 80f - 20f)
            })

            addActor(tabList
                    ?: HorizontalList<Label>(hudSkin.get("tab", HorizontalList.ListStyle::class.java)).apply {
                        tabList = this
                        val groupWidth = Gdx.graphics.width - 680f//left 480+20+60, right 80+40
                        setPosition(480f + 20f + 60f, Gdx.graphics.height - 120f, Align.left)
                        val items = Array<Label>()
                        paramsTitles.forEach {
                            items.add(Label(it, hudSkin.get("font20", Label.LabelStyle::class.java)).apply {
                                setAlignment(Align.center);setSize(groupWidth / 5f, 60f)
                            })
                        }
                        setItems(items)
                        setSize(prefWidth, prefHeight)
                        addListener(tabClickListener)
                    })

            addActor(Group().apply {
                addActor(btnDelete
                        ?: TextButton("删除服务器", hudSkin.get("font20", TextButton.TextButtonStyle::class.java)).apply {
                            isDisabled = true
                            setSize(300f, 90f)
                            setOnClickListener {
                                val i = recordList?.content?.selectedIndex ?: -1
                                itemDeleteListener?.invoke(i, currInfo as? T?)
                            }
                        }.also { btnDelete = it })
                addActor(Image(TextureRegionDrawable(hudTexture.findRegion("delete"))).apply {
                    setPosition(25f, 25f);setSize(40f, 40f)
                })
                setPosition(480f + 60f + 20f, 40f)//margin left 30, margin list 20
            })

            addActor(btnPlay
                    ?: TextButton("创建世界", hudSkin.get("font20", TextButton.TextButtonStyle::class.java)).apply {
                        setSize(300f, 90f)
                        setPosition(Gdx.graphics.width - 80f - 300f - 60f, 40f)//margin right 80, width 280, margin right 30.
                        setOnClickListener {
                            val i = recordList?.content?.selectedIndex ?: -1
                            itemPlayListener?.invoke(i, currInfo as? T?)
                        }
                    }.also { btnPlay = it })

            setStage = ParamsSettingStage()
            forestStage = ParamsForestStage()
            currParamsStage = setStage
        }
    }

    private fun <T : Actor> newListItem(info: WorlInfo, skin: Skin?, hudSkin: Skin?): T? {
        skin ?: return null
        hudSkin ?: return null

        val role = if (info.role.isNullOrEmpty()) "default" else info.role
        val bgImage = skin.get("background", Image::class.java).drawable
        val roleHead = skin.get(role, Image::class.java).apply { setSize(110f, 120f) }
        val fontDesStyle = hudSkin.get("font14", Label.LabelStyle::class.java)
        val fontTitleStyle = hudSkin.get("font18", Label.LabelStyle::class.java)
        return Table().apply {
            add(Container(roleHead).apply { background = bgImage;setSize(prefWidth, prefHeight) })
            add(Table().apply {
                align(Align.topLeft)
                add(Label("${info.name}", fontTitleStyle))
                row()
                add(Label(if (info.days <= 0) "" else "${info.days}天", fontDesStyle))
                        .padTop(5f).align(Align.left)
                setSize(prefWidth, prefHeight)
            }).padLeft(20f)
            setSize(prefWidth, prefHeight)
        } as T
    }

    fun updateRecords(list: kotlin.collections.List<WorlInfo>) {
        val skin = manager?.get(R.skin.saveslot_portraits, Skin::class.java)
        val hudSkin = manager?.get(R.skin.option_hud, Skin::class.java)
        val items = Array<Actor>()
        infoList.clear()
        list.forEach {
            newListItem<Actor>(it, skin, hudSkin)?.let { act ->
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
                val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)

                val fontTitleStyle = hudSkin.get("font16-ceab8d", Label.LabelStyle::class.java)
                val fontEtStyle = hudSkin.get("font18-black", TextField.TextFieldStyle::class.java)
                val fontBtnStyle = hudSkin.get("font18", TextButton.TextButtonStyle::class.java)
                addActor(Table().apply {
                    add(Label("游戏风格： ", fontTitleStyle))
                    btnStyle = btnStyle ?: TextButton("社交", fontBtnStyle).apply {
                        setOnClickListener {
                            setStyleStage = setStyleStage ?: newStyleSatge()
                            currParamsStage = setStyleStage
                        }
                    }
                    add(btnStyle).width(300f)
                    row()
                    add(Label("名称： ", fontTitleStyle))
                    etName = etName ?: TextField("", fontEtStyle)
                    add(etName).width(500f).padTop(25f)
                    row()
                    add(Label("描述： ", fontTitleStyle))
                    etDesc = etDesc ?: TextField("", fontEtStyle)
                    add(etDesc).width(500f).padTop(25f)
                    row()
                    add(Label("游戏模式： ", fontTitleStyle))
                    add(newModelSelectItem(hudSkin, "生存", {

                    }, {

                    })).padTop(25f)
                    row()
                    add(Label("玩家对战： ", fontTitleStyle))
                    add(newModelSelectItem(hudSkin, "关闭", {

                    }, {

                    })).padTop(25f)
                    row()
                    add(Label("玩家： ", fontTitleStyle))
                    add(newModelSelectItem(hudSkin, "4", {

                    }, {

                    })).padTop(25f)
                    row()
                    add(Label("密码： ", fontTitleStyle))
                    etPwd = etPwd ?: TextField("", fontEtStyle)
                    add(etPwd).padTop(25f).width(500f)
                    row()
                    add(Label("服务器模式： ", fontTitleStyle))
                    add(newModelSelectItem(hudSkin, "线下", {

                    }, {

                    })).padTop(25f)
                    top()
                    setSize(Gdx.graphics.width - 480f - 20f - 60f, Gdx.graphics.height - 40f - 80f - 160f + 30f)
                    setPosition(480f + 20f, 40f + 80f - 20f)
                })
            }

            if (currInfo == null) {
                setStyleStage = setStyleStage ?: newStyleSatge()
                currParamsStage = setStyleStage
            }
        }

        private fun newModelSelectItem(skin: Skin?, text: String,
                                       onPrev: (txtButton: TextButton?) -> Unit,
                                       onNext: (txtButton: TextButton?) -> Unit): Actor {
            skin ?: return Actor()
            var btnTxt: TextButton? = null
            val fontBtnStyle = skin.get("font18-ceab8d", TextButton.TextButtonStyle::class.java)
            return HorizontalGroup().apply {
                addActor(ImageButton(skin, "arrow-left").apply {
                    clearChildren();add(image).size(60f, 60f)
                    setOnClickListener { onPrev(btnTxt) }
                })
                btnTxt = btnTxt ?: TextButton(text, fontBtnStyle).apply {
                    clearChildren()
                    add(label).pad(0f, 80f, 0f, 80f).size(80f, 60f)
                }
                addActor(btnTxt)
                addActor(ImageButton(skin, "arrow-right").apply {
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
                val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)
                val imgSkin = mgr.get(R.skin.server_intentions, Skin::class.java)

                val fontF2LabelStyle = hudSkin.get("font30-f2f2f2", Label.LabelStyle::class.java)
                addActor(Table().apply {
                    val panelWidth = Gdx.graphics.width - 480f - 20f - 60f
                    var btnWidth = (panelWidth - 200f) / 4f
                    add(Label("你的服务器是什么游戏风格？", fontF2LabelStyle)).center().padTop(60f)
                    row()
                    add(Table().apply {
                        val drawable1 = imgSkin.get("social", Image::class.java).drawable
                        add(newStyleItem("社交", hudSkin, drawable1, 0)).size(btnWidth, btnWidth - 20f)
                        val drawable2 = imgSkin.get("coop", Image::class.java).drawable
                        add(newStyleItem("合作", hudSkin, drawable2, 1)).size(btnWidth, btnWidth - 20f).space(30f)
                        val drawable3 = imgSkin.get("competitive", Image::class.java).drawable
                        add(newStyleItem("竞争", hudSkin, drawable3, 2)).size(btnWidth, btnWidth - 20f).space(30f)
                        val drawable4 = imgSkin.get("madness", Image::class.java).drawable
                        add(newStyleItem("疯狂", hudSkin, drawable4, 3)).size(btnWidth, btnWidth - 20f).space(30f)
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

        private fun newStyleItem(text: String, hudSkin: Skin?, imgDrawable: Drawable, index: Int): Actor {
            hudSkin ?: return Actor()
            val style = hudSkin.get("game-style-bg", ImageTextButton.ImageTextButtonStyle::class.java).let {
                it.imageUp = hudSkin.newDrawable(imgDrawable)
                ImageTextButton.ImageTextButtonStyle(it)
            }
            return ImageTextButton(text, style).apply {
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
                val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)
                val iconTexture = mgr.get(R.image.customisation, TextureAtlas::class.java)

                val panelWidth = Gdx.graphics.width - 480f - 20f - 60f
                val panelHeight = Gdx.graphics.height - 40f - 80f - 130f
                var topHeight = 0f

                val fontBtnStyle = hudSkin.get("font20-ceab8d", TextButton.TextButtonStyle::class.java)
                addActor(VerticalGroup().apply {
                    addActor(HorizontalGroup().apply {
                        addActor(Label("森林 预设", hudSkin.get("font20-ceab8d", Label.LabelStyle::class.java)))
                        addActor(Table().apply {
                            add(ImageButton(hudSkin, "arrow-left").apply { clearChildren();add(image).size(60f, 60f) })
                            add(TextButton("默认", fontBtnStyle).apply {
                                clearChildren();add(label).pad(0f, 80f, 0f, 80f).size(220f, 60f)
                            })
                            add(ImageButton(hudSkin, "arrow-right").apply { clearChildren();add(image).size(60f, 60f) })
                            padLeft(400f)
                        })
                        pad(10f, 0f, 30f, 0f)
                        topHeight += prefHeight
                    })
                    addActor(Label("标准《饥荒》体验。", hudSkin.get("font18", Label.LabelStyle::class.java)).apply {
                        pad(0f, 50f, 15f, 50f)
                        topHeight += prefHeight
                    })
//                    addActor(Image(TextureRegionDrawable(hudTexture.findRegion("slider_line"))))

                    addActor(Group().apply {
                        addActor(ScrollPane(Table().apply {
                            newParamsPanelItems(hudSkin, iconTexture).forEach {
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
                    setPosition(480f + 20f, 40f + 80f)
                })

            }
        }

        private fun newParamsPanelItems(skin: Skin?, iconTexture: TextureAtlas): Array<Actor> {
            skin ?: return Array()
            val titleTxtStyle = skin.get("font20-empty-bg", TextButton.TextButtonStyle::class.java)
            var itemW = 0f
            return Array<Actor>().apply {
                val table = Table().apply {
                    add(newSelectItem("创世界：生物群落", "默认", skin,
                            iconTexture, "world_map", {

                    }, {

                    }))
                    add(newSelectItem("创世界：出生点", "默认", skin,
                            iconTexture, "world_start", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight);itemW = prefWidth
                }
                add(TextButton("森林 世界", titleTxtStyle).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(table)
                add(Table().apply {
                    add(newSelectItem("创世界：大小", "大", skin,
                            iconTexture, "world_size", {

                    }, {

                    }))
                    add(newSelectItem("创世界：分支", "默认", skin,
                            iconTexture, "world_branching", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("创世界：循环", "默认", skin,
                            iconTexture, "world_loop", {

                    }, {

                    }))
                    add(newSelectItem("事件", "默认", skin,
                            iconTexture, "events", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("春", "默认", skin,
                            iconTexture, "spring", {

                    }, {

                    }))
                    add(newSelectItem("夏", "默认", skin,
                            iconTexture, "summer", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })

                add(Table().apply {
                    add(newSelectItem("秋", "默认", skin,
                            iconTexture, "autumn", {

                    }, {

                    }))
                    add(newSelectItem("冬", "默认", skin,
                            iconTexture, "winter", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("起始季节", "秋", skin,
                            iconTexture, "season_start", {

                    }, {

                    }))
                    add(newSelectItem("昼夜选项", "默认", skin,
                            iconTexture, "day", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("雨", "秋", skin,
                            iconTexture, "rain", {

                    }, {

                    }))
                    add(newSelectItem("闪电", "默认", skin,
                            iconTexture, "lightning", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("青蛙雨", "秋", skin,
                            iconTexture, "frog_rain", {

                    }, {

                    }))
                    add(newSelectItem("野火", "默认", skin,
                            iconTexture, "smoke", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("世界再生", "秋", skin,
                            iconTexture, "regrowth", {

                    }, {

                    }))
                    add(newSelectItem("复活石", "默认", skin,
                            iconTexture, "resurrection", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("失败的幸存者", "秋", skin,
                            iconTexture, "skeletons", {

                    }, {

                    }))
                    add(newSelectItem("疾病", "默认", skin,
                            iconTexture, "berrybush_diseased", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("开始资源多样化", "秋", skin,
                            iconTexture, "start_resource", {

                    }, {

                    }))
                    add(newSelectItem("森林石化", "默认", skin,
                            iconTexture, "petrified_tree", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 资源", titleTxtStyle).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("花，邪恶花", "秋", skin,
                            iconTexture, "flowers", {

                    }, {

                    }))
                    add(newSelectItem("草", "默认", skin,
                            iconTexture, "grass", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树苗", "秋", skin,
                            iconTexture, "sapling", {

                    }, {

                    }))
                    add(newSelectItem("尖灌木", "默认", skin,
                            iconTexture, "marsh_bush", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("风滚草", "秋", skin,
                            iconTexture, "tumbleweeds", {

                    }, {

                    }))
                    add(newSelectItem("芦苇", "默认", skin,
                            iconTexture, "blank_grassy", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树（所有）", "秋", skin,
                            iconTexture, "trees", {

                    }, {

                    }))
                    add(newSelectItem("燧石", "默认", skin,
                            iconTexture, "flint", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("巨石", "秋", skin,
                            iconTexture, "rock", {

                    }, {

                    }))
                    add(newSelectItem("小冰川", "默认", skin,
                            iconTexture, "iceboulder", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("流星区域", "秋", skin,
                            iconTexture, "burntground", {

                    }, {

                    }))
                    add(newSelectItem("流星频率", "默认", skin,
                            iconTexture, "meteor", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 食物", titleTxtStyle).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("浆果灌木", "秋", skin,
                            iconTexture, "berrybush", {

                    }, {

                    }))
                    add(newSelectItem("胡萝卜", "默认", skin,
                            iconTexture, "carrot", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蘑菇", "秋", skin,
                            iconTexture, "mushrooms", {

                    }, {

                    }))
                    add(newSelectItem("仙人掌", "默认", skin,
                            iconTexture, "cactus", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 动物", titleTxtStyle).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("兔子", "秋", skin,
                            iconTexture, "rabbits", {

                    }, {

                    }))
                    add(newSelectItem("鼹鼠", "默认", skin,
                            iconTexture, "mole", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蝴蝶", "秋", skin,
                            iconTexture, "butterfly", {

                    }, {

                    }))
                    add(newSelectItem("鸟", "默认", skin,
                            iconTexture, "birds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("美洲鹫", "秋", skin,
                            iconTexture, "buzzard", {

                    }, {

                    }))
                    add(newSelectItem("浣猫", "默认", skin,
                            iconTexture, "catcoon", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("雄火鸡", "秋", skin,
                            iconTexture, "perd", {

                    }, {

                    }))
                    add(newSelectItem("猪", "默认", skin,
                            iconTexture, "pigs", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("伏特山羊", "秋", skin,
                            iconTexture, "lightning_goat", {

                    }, {

                    }))
                    add(newSelectItem("牦牛", "默认", skin,
                            iconTexture, "beefalo", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("牦牛交配频率", "秋", skin,
                            iconTexture, "beefaloheat", {

                    }, {

                    }))
                    add(newSelectItem("狩猎", "默认", skin,
                            iconTexture, "tracks", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("追猎惊喜", "秋", skin,
                            iconTexture, "alternatehunt", {

                    }, {

                    }))
                    add(newSelectItem("企鹅", "默认", skin,
                            iconTexture, "pengull", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("池塘", "秋", skin,
                            iconTexture, "ponds", {

                    }, {

                    }))
                    add(newSelectItem("蜜蜂", "默认", skin,
                            iconTexture, "beehive", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("杀人蜂", "秋", skin,
                            iconTexture, "wasphive", {

                    }, {

                    }))
                    add(newSelectItem("高脚鸟", "默认", skin,
                            iconTexture, "tallbirds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(TextButton("森林 怪物", titleTxtStyle).apply {
                    clearChildren();add(label).padTop(20f).padBottom(20f);setSize(itemW, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("蜘蛛", "秋", skin,
                            iconTexture, "spiders", {

                    }, {

                    }))
                    add(newSelectItem("猎犬袭击", "默认", skin,
                            iconTexture, "hounds", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("猎犬丘", "秋", skin,
                            iconTexture, "houndmound", {

                    }, {

                    }))
                    add(newSelectItem("鱼人", "默认", skin,
                            iconTexture, "merms", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("触手", "秋", skin,
                            iconTexture, "tentacles", {

                    }, {

                    }))
                    add(newSelectItem("发条装置", "默认", skin,
                            iconTexture, "chess_monsters", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("食人花", "秋", skin,
                            iconTexture, "lureplant", {

                    }, {

                    }))
                    add(newSelectItem("海象人营地", "默认", skin,
                            iconTexture, "mactusk", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("树精守卫", "秋", skin,
                            iconTexture, "liefs", {

                    }, {

                    }))
                    add(newSelectItem("毒山毛榉树", "默认", skin,
                            iconTexture, "deciduouspoison", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("坎普斯", "秋", skin,
                            iconTexture, "krampus", {

                    }, {

                    }))
                    add(newSelectItem("狂暴熊獾", "默认", skin,
                            iconTexture, "bearger", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("独眼巨鹿", "秋", skin,
                            iconTexture, "deerclops", {

                    }, {

                    }))
                    add(newSelectItem("驼鹿/鹅", "默认", skin,
                            iconTexture, "goosemoose", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
                add(Table().apply {
                    add(newSelectItem("龙蝇", "秋", skin,
                            iconTexture, "dragonfly", {

                    }, {

                    }))
                    add(newSelectItem("蚁狮贡品", "默认", skin,
                            iconTexture, "antlion_tribute", {

                    }, {

                    })).padLeft(50f)
                    setSize(prefWidth, prefHeight)
                })
            }
        }

        private fun newSelectItem(desc: String, value: String, skin: Skin?,
                                  iconTexture: TextureAtlas, iconName: String,
                                  onPrev: (valueLabel: Label?) -> Unit,
                                  onNext: (valueLabel: Label?) -> Unit): Actor {
            skin ?: return Actor()
            var valLabel: Label? = null

            val btnStyle = skin.get("option_bg", Button.ButtonStyle::class.java)
            val fontF2Style = skin.get("font18-f2f2f2", Label.LabelStyle::class.java)
            val fontCeStyle = skin.get("font14-ceab8d", Label.LabelStyle::class.java)
            return Button(btnStyle).apply {
                clearChildren()
                add(Image(TextureRegionDrawable(iconTexture.findRegion(iconName)))).size(100f, 100f)
                add(ImageButton(skin, "arrow-left").apply {
                    setOnClickListener { onPrev(valLabel) }
                }).size(60f, 90f)
                add(Table().apply {
                    add(Label(desc, fontF2Style))
                    row()
                    valLabel = valLabel ?: Label(value, fontCeStyle)
                    add(valLabel).padTop(10f)
                    setSize(prefWidth, prefHeight)
                }).size(240f, 60f).padLeft(50f).padRight(50f)
                add(ImageButton(skin, "arrow-right").apply {
                    setOnClickListener { onNext(valLabel) }
                }).size(60f, 90f)
                setSize(prefWidth, prefHeight)
            }
        }
    }
}