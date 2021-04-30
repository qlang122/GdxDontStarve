package com.qlang.game.demo.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.model.WorlListViewModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.game.demo.route.Navigator
import com.qlang.game.demo.stage.WorlListSatge
import com.qlang.game.demo.utils.Log
import com.qlang.gdxkt.lifecycle.Observer

class WorlListScreen : BaseVMScreen<WorlListViewModel> {
    private var recordsStage: WorlListSatge? = null

    init {
        recordsStage = WorlListSatge()

        recordsStage?.setOnItemClickListener {
            Log.e("QL", "------->>$it")
        }
        recordsStage?.setBackClickListener { Navigator.pop(this) }

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
        Gdx.gl.glClearColor(0.75f, 0.6f, 0.6f, 1f);
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
                viewModel.records.add(WorlInfo(1, "wx78", "机器人1,a", 5, 0, 0))
                viewModel.records.add(WorlInfo(2, "winnie", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(3, "wolfgang", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(4, "wilson", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(5, "willow", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(6, "webber", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(7, "wes", "as人1？a", 12, 0, 0))
                viewModel.records.add(WorlInfo(8, "wickerbottom", "as人1？a", 12, 0, 0))
                if (it.success) {
                    recordsStage?.updateRecords(viewModel.records)
                }
            })
        }
    }
}