package com.vk.core.presentation.view.contracts

import com.vk.core.presentation.state.ViewState


/**
 * This interface mean that screen support single valueOrInitial
 */
interface ViewWithRender<in S : ViewState> {
    /**
     * This function must be refresh all screen valueOrInitial
     */
    fun render(state: S)
}