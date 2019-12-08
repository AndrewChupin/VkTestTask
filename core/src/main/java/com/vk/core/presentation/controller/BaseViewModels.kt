package com.vk.core.presentation.controller

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import com.vk.core.presentation.state.ViewState
import com.vk.core.traits.ReactiveActions


abstract class ViewModelBase : ViewModel(), Attachable, ReactiveActions {

    private var isFirstAttach = true
    protected open var isAttached: Boolean = false

    @CallSuper
    override fun onAttach() {
        isAttached = true
        if (isFirstAttach) {
            onFirstAttach()
            isFirstAttach = false
        }
    }

    protected open fun onFirstAttach() {}

    @CallSuper
    fun onDetach() {
        isAttached = false
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        clearDisposables()
    }
}


abstract class ViewModelStateful<VS: ViewState> : ViewModelBase(), DispatcherStateful<VS> {

    abstract val stateController: StateController<VS>

    override fun getStateHolder(): StateHolder<VS> {
        return stateController
    }
}