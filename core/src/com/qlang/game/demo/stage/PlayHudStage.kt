package com.qlang.game.demo.stage

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Array
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R

class PlayHudStage : Stage() {
    private val manager: AssetManager? = GameManager.instance?.mainManager

    init {
        manager?.let { mgr ->
            val hudTexture = mgr.get(R.image.hud, TextureAtlas::class.java)
            val hudSkin = mgr.get(R.skin.hud, Skin::class.java)

            addActor(VerticalGroup().apply {
                newToolTabIcons(hudSkin).forEach { addActor(it) }
                setSize(120f, Gdx.graphics.height - 300f)
                setPosition(0f, 180f)
            })
        }
    }

    private fun newToolTabIcons(skin: Skin?): Array<Actor> {
        skin ?: return Array()
        return Array<Actor>().apply {
            add(ImageButton(skin, "tool-tool").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-light").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-trap").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-farm").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-science").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-fight").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-build").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-empty").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-tool").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-empty").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-dress").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-empty").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-empty").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
            add(ImageButton(skin, "tool-engineering").apply {
                setSize(200f, 200f)
                setOnClickListener { }
            })
        }
    }
}