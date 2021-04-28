package com.qlang.game.demo.model

import com.qlang.game.demo.db.dbExecute
import com.qlang.game.demo.entity.UiState
import com.qlang.game.demo.entity.WorlInfo
import com.qlang.game.demo.mvvm.ViewModel
import com.qlang.game.demo.utils.Log
import com.qlang.gdxkt.lifecycle.LiveData
import com.qlang.gdxkt.lifecycle.MutableLiveData
import com.qlang.gdxkt.lifecycle.Transformations
import com.qlang.gdxkt.lifecycle.liveData

class WorlListViewModel : ViewModel() {

    val records = ArrayList<WorlInfo>()

    private val recordsParams = MutableLiveData<String>()
    val recordsUiState: LiveData<UiState<Any>> = Transformations.switchMap(recordsParams) {
        liveData {
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