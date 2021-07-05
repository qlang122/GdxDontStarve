package com.qlang.game.demo.base

interface OnItemClickListener<T> {
    fun onClick(t: T?, position: Int)
}