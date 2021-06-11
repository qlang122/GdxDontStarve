package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.config.AppConfig
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.model.WorlListModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.stage.WorlRecordStage
import com.qlang.game.demo.utils.Log
import com.qlang.gdxkt.lifecycle.Observer
import games.rednblack.editor.renderer.SceneLoader
import games.rednblack.editor.renderer.components.additional.ButtonComponent
import games.rednblack.editor.renderer.resources.AsyncResourceManager
import games.rednblack.editor.renderer.utils.ComponentRetriever
import games.rednblack.editor.renderer.utils.ItemWrapper

class WorlListScreen : BaseVMScreen<WorlListModel> {
    private var recordsStage: WorlRecordStage<WorlInfo>? = null

    private var sceneLoader: SceneLoader? = null
    private lateinit var viewport: Viewport
    private val camera = OrthographicCamera()
    private var wrapper: ItemWrapper? = null

    init {
        recordsStage = WorlRecordStage<WorlInfo>().apply {
            setOnItemClickListener { index, t ->
                Log.e("QL", "------->>$index")
            }
            setOnItemPlayListener { index, t -> go2Play(t) }
            setOnItemDeleteListener { index, t -> }
        }
        recordsStage?.setBackClickListener { Navigator.pop(this) }

        viewport = ScalingViewport(Scaling.stretch, AppConfig.worldWidth, AppConfig.worldHeight, camera)
        GameManager.instance?.mainManager?.let {
            sceneLoader = SceneLoader(it.get("project.dt", AsyncResourceManager::class.java))
            sceneLoader?.loadScene("WorlParamsScene", viewport)
            wrapper = ItemWrapper(sceneLoader?.root)

            ComponentRetriever.addMapper(ButtonComponent::class.java)
            val entity = wrapper?.getChild("btn_back")?.entity
            entity?.add(sceneLoader?.engine?.createComponent(ButtonComponent::class.java))
            entity?.getComponent(ButtonComponent::class.java)?.let {
                it.addListener(object : ButtonComponent.ButtonListener {
                    override fun touchUp() {
                    }

                    override fun clicked() {
                        Log.e("QL", "------->>>>")
                    }

                    override fun touchDown() {
                    }
                })
            }

            Log.e("QL", entity?.getComponent(ButtonComponent::class.java))
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
        recordsStage?.let { GameManager.instance?.addInputProcessor(it) }
    }

    override fun hide() {
        super.hide()
        recordsStage?.let { GameManager.instance?.removeInputProcessor(it) }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        viewport.apply()
        sceneLoader?.engine?.update(Gdx.graphics.deltaTime)

//        recordsStage?.apply { act();draw() }
    }

    override fun dispose() {
        recordsStage?.dispose()
        recordsStage = null
    }

    private fun go2Play(info: WorlInfo?) {
        Navigator.push(PlayLoadingScreen())
        GameManager.instance?.apply {
            playManager.loadPlayAssets()
            playManager.finishLoading()
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
                    recordsStage?.updateRecords(viewModel.records)
                }
            })
        }
    }
}