package com.qlang.game.demo.screen

import com.qlang.game.demo.model.WorlListViewModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.gdxkt.lifecycle.Observer

class WorlListScreen : BaseVMScreen<WorlListViewModel> {

    init {

    }

    constructor() : super() {
        viewModel.getRecords()
    }

    override fun bindVM(): WorlListViewModel {
        return WorlListViewModel()
    }

    override fun show() {
        super.show()
    }

    override fun hide() {
        super.hide()
    }

    override fun observe() {
        if (!viewModel.recordsUiState.hasObservers()) {
            viewModel.recordsUiState.observe(this, Observer {
                if (it.success) {

                }
            })
        }
    }
}