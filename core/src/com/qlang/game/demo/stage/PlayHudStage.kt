package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.actor.hud.*
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.h2d.extention.spriter.SpriterItemType
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.components.MainItemComponent
import games.rednblack.editor.renderer.components.additional.ButtonComponent
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class PlayHudStage : Stage {
    private val mainManager: AssetManager? = GameManager.instance?.mainManager
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private var sceneLoader: SceneLoader? = null
    private var wrapper: ItemWrapper? = null

    private var clockActor: PlayClockActor? = null
    private var healthActor: PlayerHealthActor? = null
    private var hungerActor: PlayerHungerActor? = null
    private var sanityActor: PlayerSanityActor? = null
    private var wetMeterActor: PlayerWetMeterActor? = null

    private val toolBtnMaps: HashMap<Int, ButtonComponent> = hashMapOf()
    private val goodsBtnMaps: HashMap<Int, ButtonComponent> = hashMapOf()

    constructor() : super(ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)) {
        mainManager?.let { mgr ->
            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.injectExternalItemType(SpriterItemType())
            sceneLoader?.loadScene("PlayHudScene", viewport)
            sceneLoader?.addComponentByTagName("button", ButtonComponent::class.java)

            wrapper = ItemWrapper(sceneLoader?.root)

            initToolButtons()
            initGoodsButtons()

            wrapper?.getChild("ly_act")?.entity?.let {
                val lyAct = ItemWrapper(it)
                setActionsClick(lyAct.getChild("btn_map"), 1)
                setActionsClick(lyAct.getChild("btn_pause"), 2)
                setActionsClick(lyAct.getChild("btn_arrow_left"), 3)
                setActionsClick(lyAct.getChild("btn_arrow_right"), 4)
            }

            wrapper?.getChild("ly_playerState")?.entity?.let {
                val lyState = ItemWrapper(it)
                clockActor = PlayClockActor(lyState.getChild("iv_clock")?.entity, lyState.getChild("tv_days")?.entity)
                healthActor = PlayerHealthActor(lyState.getChild("iv_hunger")?.entity, lyState.getChild("iv_hunger_arrow")?.entity)
                hungerActor = PlayerHungerActor(lyState.getChild("iv_health")?.entity, lyState.getChild("iv_health_arrow")?.entity)
                sanityActor = PlayerSanityActor(lyState.getChild("iv_sanity")?.entity, lyState.getChild("iv_sanity_arrow")?.entity)
                wetMeterActor = PlayerWetMeterActor(lyState.getChild("iv_wet_meter")?.entity, lyState.getChild("iv_wet_meter_arrow")?.entity)
            }
        }
    }

    private fun initToolButtons() {
        wrapper?.getChild("ly_tools")?.entity?.let {
            val lyTool = ItemWrapper(it)
            setToolItemsClick(lyTool.getChild("btn_tool"), 1)
            setToolItemsClick(lyTool.getChild("btn_fire"), 2)
            setToolItemsClick(lyTool.getChild("btn_trap"), 3)
            setToolItemsClick(lyTool.getChild("btn_farm"), 4)
            setToolItemsClick(lyTool.getChild("btn_science"), 5)
            setToolItemsClick(lyTool.getChild("btn_fight"), 6)
            setToolItemsClick(lyTool.getChild("btn_build"), 7)
            setToolItemsClick(lyTool.getChild("btn_seafaring"), 8)
            setToolItemsClick(lyTool.getChild("btn_refine"), 9)
            setToolItemsClick(lyTool.getChild("btn_arcane"), 10)
            setToolItemsClick(lyTool.getChild("btn_dress"), 11)
            setToolItemsClick(lyTool.getChild("btn_gemology"), 12)
            setToolItemsClick(lyTool.getChild("btn_crafting_table"), 13)
            setToolItemsClick(lyTool.getChild("btn_other"), 14)
        }
    }

    private fun initGoodsButtons() {
        wrapper?.getChild("ly_goods")?.entity?.let {
            val lyGoods = ItemWrapper(it)
            setGoodsItemsClick(lyGoods.getChild("btn_goods_hand"), 0x101)
            setGoodsItemsClick(lyGoods.getChild("btn_goods_body"), 0x102)
            setGoodsItemsClick(lyGoods.getChild("btn_goods_head"), 0x103)
            setGoodsItemsClick(lyGoods.getChild("btn_goods_pack"), 0x104)
            setGoodsItemsClick(lyGoods.getChild("btn_goods_gemstone"), 0x105)

            for (i in 1..44) {
                setGoodsItemsClick(lyGoods.getChild("btn_goods_$i"), i)
            }
        }
    }

    private fun setToolItemsClick(item: ItemWrapper?, index: Int) {
        item?.entity?.getComponent(ButtonComponent::class.java)?.let {
            toolBtnMaps[index] = it
            it.setOnClickListener { onToolItemsClicked(index) }
        }
    }

    private fun setGoodsItemsClick(item: ItemWrapper?, index: Int) {
        item?.entity?.getComponent(ButtonComponent::class.java)?.let {
            goodsBtnMaps[index] = it
            it.setOnClickListener { onGoodsItemsClicked(index) }
        }
    }

    private fun setActionsClick(item: ItemWrapper?, index: Int) {
        item?.entity?.getComponent(ButtonComponent::class.java)?.let {
            it.setOnClickListener { onActionsClicked(index) }
        }
    }

    private fun onToolItemsClicked(index: Int) {
        when (index) {
            1 -> {
            }
        }
    }

    private fun onGoodsItemsClicked(index: Int) {
        when (index) {
            1 -> {
            }
        }
    }

    private fun onActionsClicked(index: Int) {
        when (index) {
            1 -> {
            }
        }
    }

    override fun draw() {
        super.draw()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)
    }

    override fun dispose() {
        super.dispose()
        sceneLoader?.dispose()
    }
}