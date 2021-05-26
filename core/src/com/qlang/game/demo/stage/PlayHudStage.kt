package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Array
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.actor.hud.*
import com.qlang.game.demo.entity.HudTabGoodsAttr
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.widget.VerticalWidgetList

class PlayHudStage : Stage() {
    private val mainManager: AssetManager? = GameManager.instance?.mainManager
    private val playManager: AssetManager? = GameManager.instance?.playManager

    private val tabToolKeys = arrayOf("tool-tool", "tool-light", "tool-trap", "tool-farm",
            "tool-science", "tool-fight", "tool-build", "tool-seafaring", "tool-refine",
            "tool-arcane", "tool-dress", "tool-empty", "tool-crafting_table", "tool-cartography"
    )

    private var clockActor: PlayClockActor? = null
    private var healthActor: PlayerHealthActor? = null
    private var hungerActor: PlayerHungerActor? = null
    private var sanityActor: PlayerSanityActor? = null
    private var wetMeterActor: PlayerWetMeterActor? = null

    private val tabGoodsBottomArrays: ArrayList<ArrayList<HudTabGoodsAttr>> = arrayListOf(
            ArrayList<HudTabGoodsAttr>().apply {
                for (i in 0 until 10) add(HudTabGoodsAttr("tab-goods_item_n", null, 0))
                add(HudTabGoodsAttr("tab-goods_item_hand", null, 0))
                add(HudTabGoodsAttr("tab-goods_item_head", null, 0))
                add(HudTabGoodsAttr("tab-goods_item_body", null, 0))
                add(HudTabGoodsAttr("tab-goods_item_pack", null, 0))
                add(HudTabGoodsAttr("tab-goods_item_gemstone", null, 0))
                for (i in 0 until 5) add(HudTabGoodsAttr("tab-goods_item_n", null, 0))
            },
            ArrayList<HudTabGoodsAttr>().apply {
                for (i in 0 until 20) add(HudTabGoodsAttr("tab-goods_item_n", null, 0))
            }
    )

    init {
        mainManager?.let { mgr ->
            val hudSkin = mgr.get(R.skin.hud, Skin::class.java)

            addActor(VerticalWidgetList<Button>(hudSkin, "tool-tab", hudSkin, "tool-tab").apply {
                style?.background?.let { it.topHeight += 23f;it.bottomHeight += 23f;it.rightWidth += 15f }
                content?.style?.selection?.let { it.bottomHeight += 10f }
                isShowSelection = true
                content?.setSelectionRequired(false)
                content?.setItems(newToolTabIcons(hudSkin))
                val h = Gdx.graphics.height - 140f
                setPosition(0f, Gdx.graphics.height / 2f - h / 2f)
                setSize(100f, h)
                content?.setOnClickListener {
                    val item = content?.selected?.apply { isChecked = !isChecked }
                }
            })

            val turnStyle = hudSkin.get("btn-turnarrow", ImageButton.ImageButtonStyle::class.java)
            addActor(Table().apply {
                add(ImageButton(ImageButton.ImageButtonStyle().apply {
                    up = trycatch { (turnStyle.up as? TextureRegionDrawable?) }?.let {
                        TextureRegionDrawable(TextureRegion(it.region).apply { flip(true, false) })
                    }
                    down = trycatch { (turnStyle.down as? TextureRegionDrawable?) }?.let {
                        TextureRegionDrawable(TextureRegion(it.region).apply { flip(true, false) })
                    }
                    over = trycatch { (turnStyle.over as? TextureRegionDrawable?) }?.let {
                        TextureRegionDrawable(TextureRegion(it.region).apply { flip(true, false) })
                    }
                }).apply {
                    clearChildren();add(image).size(80f, 80f)
                    style?.up?.minWidth = 0f;style?.up?.minHeight = 0f
                    style?.down?.minWidth = 0f;style?.down?.minHeight = 0f
                    style?.over?.minWidth = 0f;style?.over?.minHeight = 0f
                    setSize(prefWidth, prefHeight)
                }).padBottom(5f)
                add(ImageButton(hudSkin, "btn-pause").apply {
                    clearChildren();add(image).size(70f, 70f)
                    style?.up?.minWidth = 0f;style?.up?.minHeight = 0f
                    setSize(prefWidth, prefHeight)
                }).padLeft(5f).padRight(5f)
                add(ImageButton(turnStyle).apply {
                    clearChildren();add(image).size(80f, 80f)
                    style?.up?.minWidth = 0f;style?.up?.minHeight = 0f
                    style?.down?.minWidth = 0f;style?.down?.minHeight = 0f
                    style?.over?.minWidth = 0f;style?.over?.minHeight = 0f
                    setSize(prefWidth, prefHeight)
                }).padBottom(5f)
                setSize(prefWidth, prefHeight)
                setPosition(Gdx.graphics.width - prefWidth - 20f, 10f)
            })
            addActor(ImageButton(hudSkin, "btn-map").apply {
                clearChildren();add(image).size(100f, 100f)
                style?.up?.minWidth = 0f;style?.up?.minHeight = 0f
                setSize(prefWidth, prefHeight)
                setPosition(Gdx.graphics.width - 100f / 2f - 120f - 20f, 80f + 10f)
            })

            var tabWidth = 0f
            addActor(ScrollPane(Table().apply {
                tabGoodsBottomArrays[0].forEachIndexed { i, it ->
                    add(ImageButton(hudSkin, it.icon)).size(80f, 80f).padLeft(if (i % 5 == 0) 10f else 5f)
                }
                row().padTop(5f)
                tabGoodsBottomArrays[1].forEachIndexed { i, it ->
                    add(ImageButton(hudSkin, it.icon)).size(80f, 80f).padLeft(if (i % 5 == 0) 10f else 5f)
                }
                tabWidth = prefWidth
                setSize(prefWidth, prefHeight)
            }, hudSkin, "goods-tab").apply {
                setScrollingDisabled(false, true)
                style?.background?.let { it.leftWidth += 20f;it.rightWidth += 20f;it.topHeight += 25f;it.bottomHeight += 10f }
                val width = Gdx.graphics.width
                val b = tabWidth > (width - 300f)
                setSize(if (b) (width - 150f - 240f - 10f) else (tabWidth + 50f), 180f)
                setPosition(if (b) 150f else ((width - prefWidth) / 2f - 50f), 0f)
            })

            clockActor = PlayClockActor(hudSkin).apply { pack();setSize(140f, 140f) }
            playManager?.let {
                healthActor = PlayerHealthActor(it).apply { setSize(100f, 100f) }
                hungerActor = PlayerHungerActor(it).apply { setSize(100f, 100f) }
                sanityActor = PlayerSanityActor(it).apply { setSize(100f, 100f) }
                wetMeterActor = PlayerWetMeterActor(it).apply { setSize(100f, 100f) }
            }
            addActor(Table().apply {
                val tX = Gdx.graphics.width / 2f - prefWidth / 2 - 30f
                val tY = Gdx.graphics.height / 2f - prefHeight / 2 - 15f
                add(clockActor);row().pad(0f)
                add(hungerActor).apply {
                    pack();
                    Log.e("QL", "--1-->", tX, tY, actorX, actorY, padLeft, padBottom)
                    hungerActor?.setPosition(tX + actorX + padLeft, tY + actorY + padBottom)
                }
                add(healthActor).padLeft(20f).apply {
                    pack();
                    Log.e("QL", "--2-->", tX, tY, actorX, actorY, padLeft, padBottom)
                    healthActor?.setPosition(tX + actorX + padLeft, tY + actorY + padBottom)
                }
                row().pad(0f)
                add(sanityActor).apply {
                    pack();
                    Log.e("QL", "--3-->", tX, tY, actorX, actorY, padLeft, padBottom)
                    sanityActor?.setPosition(tX + actorX + padLeft, tY + actorY + padBottom)
                }
                row().pad(0f)
                add(wetMeterActor).apply {
                    pack();
                    Log.e("QL", "--4-->", tX, tY, actorX, actorY, padLeft, padBottom)
                    wetMeterActor?.setPosition(tX + actorX + padLeft, tY + actorY + padBottom)
                }
                pack()
                setPosition(tX, tY)
            })
            addActor(healthActor)
        }
    }

    private fun newToolTabIcons(skin: Skin?): Array<Actor> {
        skin ?: return Array()
        return Array<Actor>().apply { tabToolKeys.forEach { add(ImageButton(skin, it).apply { setSize(80f, 80f) }) } }
    }
}