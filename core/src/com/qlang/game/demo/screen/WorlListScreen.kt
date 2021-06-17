package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.model.WorlListModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.game.demo.res.R
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.widget.VerticalWidgetList
import com.qlang.gdxkt.lifecycle.Observer
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.TransformComponent
import games.rednblack.editor.renderer.components.additional.ButtonComponent
import games.rednblack.editor.renderer.components.label.LabelComponent
import games.rednblack.editor.renderer.data.CompositeItemVO
import games.rednblack.editor.renderer.data.LayerItemVO
import games.rednblack.editor.renderer.data.MainItemVO
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.scene2d.CompositeActor
import games.rednblack.editor.renderer.utils.ItemWrapper

class WorlListScreen : BaseVMScreen<WorlListModel> {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    private lateinit var seceenStage: Stage

    private var recordList: VerticalWidgetList<Actor>? = null
    private val infoList = ArrayList<WorlInfo>()
    private var currInfo: WorlInfo? = null

    private var sceneLoader: SceneLoader? = null
    private lateinit var viewport: Viewport
    private var wrapper: ItemWrapper? = null

    private var btnDelete: ButtonComponent? = null
    private var tvPlay: LabelComponent? = null
    private var tvGameStyle: LabelComponent? = null

    private var layerStyle: LayerItemVO? = null
    private var layerStyleMode: LayerItemVO? = null
    private var layerStyleForest: LayerItemVO? = null

    private var btnSetting: ButtonComponent? = null
    private var btnForest: ButtonComponent? = null
    private var btnCave: ButtonComponent? = null
    private var btnModules: ButtonComponent? = null
    private var btnRecords: ButtonComponent? = null

    private var paramsScrollPane: ScrollPane? = null

    init {

        viewport = ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)
        seceenStage = Stage(viewport)

        manager?.let { mgr ->
            val hudSkin = mgr.get(R.skin.option_hud, Skin::class.java)

            val listStyle = hudSkin.get(VerticalWidgetList.WidgetListStyle::class.java)
            recordList = VerticalWidgetList<Actor>(ScrollPane.ScrollPaneStyle(), listStyle.apply {
                selection = trycatch { hudSkin.newDrawable(selection) }?.apply { leftWidth += 20f;topHeight += 15f;bottomHeight += 15f }
            })

            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.loadScene("WorlParamsScene", viewport)
            sceneLoader?.addComponentByTagName("button", ButtonComponent::class.java)

            wrapper = ItemWrapper(sceneLoader?.root)

            wrapper?.getChild("btn_back")?.entity?.getComponent(ButtonComponent::class.java)?.let {
                it.setOnClickListener { Navigator.pop(this) }
            }
            wrapper?.getChild("btn_delete")?.entity?.getComponent(ButtonComponent::class.java)?.also {
                btnDelete = it;it.setOnClickListener { }
            }
            wrapper?.getChild("btn_start")?.entity?.let {
                tvPlay = ItemWrapper(it).getChild("tv_play")?.entity?.getComponent(LabelComponent::class.java)
                it.getComponent(ButtonComponent::class.java)?.let {
                    it.setOnClickListener { go2Play(currInfo) }
                }
            }
            applyListSize()

            layerStyle = sceneLoader?.sceneVO?.composite?.getLayerByName("game_style")
            layerStyleMode = sceneLoader?.sceneVO?.composite?.getLayerByName("gamestyle_mode")
            layerStyleForest = sceneLoader?.sceneVO?.composite?.getLayerByName("gamestyle_forest")
            layerStyle?.isVisible = true

            val lySetting = wrapper?.getChild("ly_setting")
            lySetting?.entity?.let {
                ItemWrapper(it).getChild("ly_style")?.entity?.let {
                    ItemWrapper(it).getChild("btn_style")?.entity?.let {
                        it.getComponent(ButtonComponent::class.java)?.setOnClickListener {
                            if (layerStyle?.isVisible == true) {
                                layerStyle?.isVisible = false
                                layerStyleMode?.isVisible = true
                            }
                        }
                        tvGameStyle = ItemWrapper(it).getChild("tv_gameStyle")?.entity?.getComponent(LabelComponent::class.java)
                    }
                }
            }

            val tvModeTypeTips = wrapper?.getChild("tv_mode_tips")?.entity?.getComponent(LabelComponent::class.java)
            wrapper?.getChild("ly_mode")?.entity?.let {
                ItemWrapper(it).getChild("btn_style_social")?.entity?.getComponent(ButtonComponent::class.java)
                        ?.setOnClickListener { onStyleModeClick(1) }
                ItemWrapper(it).getChild("btn_style_coop")?.entity?.getComponent(ButtonComponent::class.java)
                        ?.setOnClickListener { onStyleModeClick(2) }
                ItemWrapper(it).getChild("btn_style_competitive")?.entity?.getComponent(ButtonComponent::class.java)
                        ?.setOnClickListener { onStyleModeClick(3) }
                ItemWrapper(it).getChild("btn_style_madness")?.entity?.getComponent(ButtonComponent::class.java)
                        ?.setOnClickListener { onStyleModeClick(4) }
            }

            wrapper?.getChild("ly_tab")?.entity?.let {
                btnSetting = ItemWrapper(it).getChild("tab_setting")?.entity?.getComponent(ButtonComponent::class.java)
                btnForest = ItemWrapper(it).getChild("tab_forest")?.entity?.getComponent(ButtonComponent::class.java)
                btnCave = ItemWrapper(it).getChild("tab_cave")?.entity?.getComponent(ButtonComponent::class.java)
                btnModules = ItemWrapper(it).getChild("tab_modules")?.entity?.getComponent(ButtonComponent::class.java)
                btnRecords = ItemWrapper(it).getChild("tab_records")?.entity?.getComponent(ButtonComponent::class.java)
                btnSetting?.isChecked = true
                btnSetting?.setOnClickListener { onTabItemClick(1) }
                btnForest?.setOnClickListener { onTabItemClick(2) }
                btnCave?.setOnClickListener { onTabItemClick(3) }
                btnModules?.setOnClickListener { onTabItemClick(4) }
                btnRecords?.setOnClickListener { onTabItemClick(5) }
            }

            wrapper?.getChild("ly_forest_params")?.entity?.let {
                val paramsActor = CompositeActor(CompositeItemVO().apply { loadFromEntity(it) }, sceneLoader?.rm)
                paramsScrollPane = ScrollPane(paramsActor).apply {
                    setScrollingDisabled(true, false)
                    isVisible = false
                }
                applyForestContentSize()
            }

            seceenStage.addActor(recordList)
            seceenStage.addActor(paramsScrollPane)
        }
    }

    constructor() : super() {
        viewModel.getRecords()
    }

    override fun bindVM(): WorlListModel {
        return WorlListModel()
    }

    override fun show() {
        super.show()
        GameManager.instance?.addInputProcessor(seceenStage)
    }

    override fun hide() {
        super.hide()
        GameManager.instance?.removeInputProcessor(seceenStage)
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        applyListSize()
        applyForestContentSize()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.camera.update()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)

        seceenStage.apply { act();draw() }
    }

    override fun dispose() {
        seceenStage.dispose()
        sceneLoader?.dispose()
    }

    private fun go2Play(info: WorlInfo?) {
        Navigator.push(PlayLoadingScreen())

        GameManager.instance?.apply {
            playManager.loadPlayAssets()
        }
    }

    private fun onStyleModeClick(index: Int) {
        if (layerStyleMode?.isVisible != true) return

        layerStyle?.isVisible = true
        layerStyleMode?.isVisible = false
        val txt = when (index) {
            1 -> "社交"
            2 -> "合作"
            3 -> "竞争"
            4 -> "疯狂"
            else -> "社交"
        }
        tvGameStyle?.setText(txt)
    }

    private fun onTabItemClick(index: Int) {
        btnSetting?.isChecked = false
        btnForest?.isChecked = false
        btnCave?.isChecked = false
        btnModules?.isChecked = false
        btnRecords?.isChecked = false

        val isShowMode = layerStyleMode?.isVisible ?: false
        layerStyle?.isVisible = false
        layerStyleMode?.isVisible = false
        layerStyleForest?.isVisible = false
        paramsScrollPane?.isVisible = false

        when (index) {
            1 -> {
                btnSetting?.isChecked = true
                if (isShowMode)
                    layerStyleMode?.isVisible = true
                else layerStyle?.isVisible = true
            }
            2 -> {
                btnForest?.isChecked = true
                layerStyleForest?.isVisible = true
                paramsScrollPane?.isVisible = true
            }
            3 -> {
                btnCave?.isChecked = true
            }
            4 -> {
                btnModules?.isChecked = true
            }
            5 -> {
                btnRecords?.isChecked = true
            }
        }
    }

    private fun applyListSize() {
        wrapper?.getChild("list_record")?.entity?.let {
            val vo = MainItemVO().apply { loadFromEntity(it) }
            val dime = it.getComponent(DimensionsComponent::class.java)

            val sX = Gdx.graphics.width.plus(0f) / viewport.screenWidth
            val sY = Gdx.graphics.height.plus(0f) / viewport.screenHeight

            val width = dime?.width?.times(vo.scaleX)?.times(sX)
                    ?: 400f
            val height = dime?.height?.times(vo.scaleY)?.times(sY)
                    ?: 820f
            recordList?.setSize(width, height)
            recordList?.setPosition(vo.x, vo.y)
//            Log.e("QL", "------->", width, height, vo.x, vo.y, dime?.width, dime?.height, vo?.scaleX, vo?.scaleY)
        }
    }

    private fun applyForestContentSize() {
        wrapper?.getChild("ly_forest_content")?.entity?.let {
            val vo = MainItemVO().apply { loadFromEntity(it) }
            val dime = it.getComponent(DimensionsComponent::class.java)
            val tran = it.getComponent(TransformComponent::class.java)

            val sX = Gdx.graphics.width.plus(0f) / viewport.screenWidth
            val sY = Gdx.graphics.height.plus(0f) / viewport.screenHeight

            val width = dime?.width?.times(vo.scaleX)?.times(sX)
                    ?: 700f
            val height = dime?.height?.times(vo.scaleY)?.times(sY)
                    ?: 420f
            paramsScrollPane?.setSize(width, height)
            paramsScrollPane?.setPosition(vo.x - width / 2 + vo.originX, vo.y - height / 2 + vo.originY)
        }
    }

    override fun observe() {
        if (!viewModel.recordsUiState.hasObservers()) {
            viewModel.recordsUiState.observe(this, Observer {
                viewModel.records.add(WorlInfo(1, "wx78", "机器人1,a", 5, 0, 0))
                viewModel.records.add(WorlInfo(2, "winnie", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(3, "", "新世界", -1, 0, 0))
                viewModel.records.add(WorlInfo(4, "", "新世界", -1, 0, 0))
                viewModel.records.add(WorlInfo(5, "", "新世界", -1, 0, 0))
                if (it.success) {
                    updateRecords(viewModel.records)
                }
            })
        }
    }

    private fun <T : Actor> newListItem(info: WorlInfo, skin: Skin?, hudSkin: Skin?): T? {
        skin ?: return null
        hudSkin ?: return null

        val role = if (info.role.isNullOrEmpty()) "default" else info.role
        val bgImage = skin.get("background", Image::class.java).drawable
        val roleHead = skin.get(role, Image::class.java).apply { setSize(80f, 100f) }
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
                if (b) {
                    btnDelete?.isEnable = false
                    tvPlay?.setText("创建世界")
                } else {
                    btnDelete?.isEnable = true
                    tvPlay?.setText("回到世界")
                }

            }
        })
        if (infoList.size > 0) currInfo = infoList[0]
        if (infoList.size > 0 && infoList[0].days > 0) {
            btnDelete?.isEnable = true
            tvPlay?.setText("回到世界")
        }
    }
}