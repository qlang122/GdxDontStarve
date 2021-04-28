package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.model.WorlListViewModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.game.demo.stage.WorlListSatge
import com.qlang.game.demo.utils.Log
import com.qlang.gdxkt.lifecycle.Observer

class WorlListScreen : BaseVMScreen<WorlListViewModel> {
    private var recordsStage: WorlListSatge? = null

    init {
        recordsStage = WorlListSatge().apply {
            setOnItemClickListener {
                Log.e("QL", "------->>$it")
            }
        }

    }

    constructor() : super() {
        viewModel.getRecords()
    }

    override fun bindVM(): WorlListViewModel {
        return WorlListViewModel()
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
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        recordsStage?.apply { act();draw() }
    }

    override fun dispose() {
        recordsStage?.dispose()
        recordsStage = null
    }

    override fun observe() {
        if (!viewModel.recordsUiState.hasObservers()) {
            viewModel.recordsUiState.observe(this, Observer {
                if (it.success) {
                    recordsStage?.updateRecords(viewModel.records)
                }
            })
        }
    }
}