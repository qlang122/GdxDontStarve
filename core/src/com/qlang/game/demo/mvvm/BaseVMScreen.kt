package com.qlang.game.demo.mvvm

abstract class BaseVMScreen<VM : ViewModel>() : LifecycleScreenAdapter() {

    protected val viewModel: VM by lazy { observe();bindVM() }

    abstract fun bindVM(): VM
    abstract fun observe()

}