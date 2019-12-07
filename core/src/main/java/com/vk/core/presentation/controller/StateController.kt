package com.vk.core.presentation.controller

import com.vk.core.presentation.state.ViewState


interface StateHolder<VS: ViewState> {
    val state: VS
}

interface StateController<VS: ViewState> : StateHolder<VS> {
    fun createState(): VS
}
