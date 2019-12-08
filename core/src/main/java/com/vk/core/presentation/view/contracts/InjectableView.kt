package com.vk.core.presentation.view.contracts


/**
 * Interface mean that object using Dependency Injection
 * </br>
 * @author Andrew Chupin
 */
interface InjectableView {

    /**
     * Method for initialization DI container
     */
    fun onInject()
}
