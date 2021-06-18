package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Dialog
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.ktx.setOnClickListener
import com.qlang.game.demo.ktx.trycatch
import com.qlang.game.demo.res.R
import com.qlang.game.demo.route.Navigator
import com.qlang.h2d.extention.spriter.SpriterItemType
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.components.DimensionsComponent
import games.rednblack.editor.renderer.components.additional.ButtonComponent
import games.rednblack.editor.renderer.data.CompositeItemVO
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.scene2d.CompositeActor
import games.rednblack.editor.renderer.utils.ItemWrapper

class HomeScreen : ScreenAdapter() {
    private lateinit var stage: Stage
    private var exitDialog: Dialog? = null

    private var sceneLoader: SceneLoader? = null
    private lateinit var viewport: Viewport
    private var wrapper: ItemWrapper? = null

    init {
        viewport = ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight)
        stage = Stage(viewport)

        GameManager.instance?.mainManager?.let { mgr ->
            val bitmapFont = mgr.trycatch {
                get(R.font.font_cn, BitmapFont::class.java)
            }

            sceneLoader = SceneLoader(mgr.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.injectExternalItemType(SpriterItemType())
            sceneLoader?.loadScene("MainScene", viewport)
            sceneLoader?.addComponentByTagName("button", ButtonComponent::class.java)

            wrapper = ItemWrapper(sceneLoader?.root)

            wrapper?.getChild("btn_browse")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(0) }
            wrapper?.getChild("btn_create")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(1) }
            wrapper?.getChild("btn_collection")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(2) }
            wrapper?.getChild("btn_option")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(3) }
            wrapper?.getChild("btn_mod")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(4) }
            wrapper?.getChild("btn_exit")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener { navigation2Menu(5) }

            wrapper?.getChild("ly_exit")?.entity?.let {
                ItemWrapper(it).getChild("btn_ok")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener {
                    Gdx.app.exit()
                }
                ItemWrapper(it).getChild("btn_cancel")?.entity?.getComponent(ButtonComponent::class.java)?.setOnClickListener {
                    exitDialog?.hide()
                }
                val dimen = it.getComponent(DimensionsComponent::class.java)

                val actor = CompositeActor(CompositeItemVO().apply { loadFromEntity(it) }, sceneLoader?.rm)
                exitDialog = Dialog("", Window.WindowStyle().apply {
                    this.titleFont = bitmapFont
                    background.minWidth = dimen.width
                    background.minHeight = dimen.height
                }).apply {
                    clearChildren()
                    add(actor).expand().fill()
                }
            }
        }
    }

    private fun navigation2Menu(index: Int) {
        when (index) {
            0 -> {
            }
            1 -> {
                Navigator.push(WorlListScreen())
            }
            2 -> {
            }
            3 -> {
            }
            4 -> {
            }
            5 -> {
                exitDialog?.show(stage)
            }
        }
    }

    override fun show() {
        super.show()
    }

    override fun hide() {
        super.hide()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.camera.update()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)

        stage.apply { act();draw() }
    }

    override fun dispose() {
        stage.dispose()
        sceneLoader?.dispose()
        exitDialog = null
    }
}