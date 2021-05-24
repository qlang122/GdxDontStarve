package com.qlang.game.demo.model

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.qlang.game.demo.db.dbExecute
import com.qlang.game.demo.entity.UiState
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.mvvm.ViewModel
import com.qlang.game.demo.utils.Log
import com.qlang.gdxkt.lifecycle.LiveData
import com.qlang.gdxkt.lifecycle.MutableLiveData
import com.qlang.gdxkt.lifecycle.Transformations
import com.qlang.gdxkt.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

class WorlListModel : ViewModel() {
    private val platform = Gdx.app.type

    val records = ArrayList<WorlInfo>()

    private val recordsParams = MutableLiveData<String>()
    val recordsUiState: LiveData<UiState<Any>> = Transformations.switchMap(recordsParams) {
        liveData(disp = when (platform) {
            Application.ApplicationType.Android -> Dispatchers.Main
            else -> Dispatchers.Default
        }) {
            val list = dbExecute { db ->
                db.worlDao().getWorls()
            }?.let { records.clear();records.addAll(it) }
            emit(UiState<Any>(list != null))
        }
    }

    fun getRecords() {
        recordsParams.value = "aaaaa"
    }
}