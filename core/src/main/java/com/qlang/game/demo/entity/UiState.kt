package com.qlang.game.demo.entity

data class UiState<T>(val success: Boolean = false, val msg: String? = "", var t: T? = null) {
    override fun toString(): String {
        return "UiState(success=$success, msg=$msg, t=$t)"
    }
}