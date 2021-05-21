package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Array
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.widget.VerticalWidgetList

class PlayHudStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    init {
        manager?.let { mgr ->
            val hudTexture = mgr.get(R.image.hud, TextureAtlas::class.java)
            val hudSkin = mgr.get(R.skin.hud, Skin::class.java)

            addActor(VerticalWidgetList<Actor>(hudSkin, "default", hudSkin, "default").apply {
                isShowSelection = true
                content?.setItems(newToolTabIcons(hudSkin))
                val h = Gdx.graphics.height - 200f
                setPosition(0f, Gdx.graphics.height / 2f - h / 2f)
                setSize(100f, h)
            })
        }
    }

    private fun newToolTabIcons(skin: Skin?): Array<Actor> {
        skin ?: return Array()
        return Array<Actor>().apply {
            add(ImageButton(skin, "tool-tool").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-light").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-trap").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-farm").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-science").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-fight").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-build").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-seafaring").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-refine").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-arcane").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-dress").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-empty").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-crafting_table").apply {
                setSize(80f, 80f)
            })
            add(ImageButton(skin, "tool-cartography").apply {
                setSize(80f, 80f)
            })
        }
    }
}