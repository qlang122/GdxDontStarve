package com.qlang.game.demo.model

import com.qlang.game.demo.entity.UiState
import com.qlang.game.demo.mvvm.ViewModel
import com.qlang.gdxkt.lifecycle.LiveData
import com.qlang.gdxkt.lifecycle.MutableLiveData
import com.qlang.gdxkt.lifecycle.Transformations
import com.qlang.gdxkt.lifecycle.liveData
import kotlinx.coroutines.delay

class WorlListViewModel : ViewModel() {

    private val recordsParams = MutableLiveData<String>()
    val recordsUiState: LiveData<UiState<Any>> = Transformations.switchMap(recordsParams) {
        liveData {
            delay(2000)
            emit(UiState<Any>(true))
        }
    }

    fun getRecords() {
        recordsParams.value = ""
    }
}