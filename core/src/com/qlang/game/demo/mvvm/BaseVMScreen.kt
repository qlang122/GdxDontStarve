package com.qlang.game.demo.mvvm

abstract class BaseVMScreen<VM : ViewModel> : LifecycleScreenAdapter {

    protected lateinit var viewModel: VM

    constructor() : super() {
        viewModel = bindVM()
        observe()
    }

    abstract fun bindVM(): VM
    abstract fun observe()

}