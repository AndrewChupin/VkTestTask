package com.vk.core.presentation.view.contracts

import com.vk.core.presentation.controller.Dispatcher


/**
 * This interface mean that object using controller as
 * </br>
 * @author Andrew Chupin
 */
interface ViewWithDispatcher<D : Dispatcher> {

    /**
     * You can provide with [javax.inject.Inject] annotation
     */
    var dispatcher: D
}