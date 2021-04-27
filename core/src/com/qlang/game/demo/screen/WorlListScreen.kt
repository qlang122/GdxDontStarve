package com.qlang.game.demo.screen

import com.badlogic.gdx.assets.AssetManager
import com.qlang.game.demo.GameManager
import com.qlang.game.demo.model.WorlListViewModel
import com.qlang.game.demo.mvvm.BaseVMScreen
import com.qlang.game.demo.utils.Log
import com.qlang.game.demo.widget.MyList
import com.qlang.gdxkt.lifecycle.Observer

class WorlListScreen : BaseVMScreen<WorlListViewModel>() {
    private val manager: AssetManager? = GameManager.instance?.mainManager
    private lateinit var list: MyList<String>

    init {
        manager?.let { mgr ->


        }
    }

    override fun bindVM(): WorlListViewModel {
        return WorlListViewModel()
    }

    override fun observe() {
        viewModel.recordsUiState.observe(this, Observer {
            Log.e("QL", "-------------$it")
        })
    }
}