package com.vk.core.presentation.controller

import com.vk.core.presentation.state.ViewState


interface Attachable {
    fun onAttach()
}

interface Detachable {
    fun onDetach()
}

interface Dispatcher : Attachable, Detachable


interface DispatcherStateful<VS : ViewState> : Dispatcher {
    fun getStateHolder(): StateHolder<VS>
}
