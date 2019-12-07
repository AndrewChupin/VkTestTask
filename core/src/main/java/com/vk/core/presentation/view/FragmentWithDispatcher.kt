package com.vk.core.presentation.view

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import com.vk.core.presentation.controller.Dispatcher
import com.vk.core.presentation.view.contracts.InjectableView
import com.vk.core.presentation.view.contracts.ViewWithDispatcher
import javax.inject.Inject


abstract class FragmentWithDispatcher<D : Dispatcher, V: View>
    : BaseFragment<V>(), InjectableView, ViewWithDispatcher<D> {

    @Inject
    override lateinit var dispatcher: D

    @CallSuper
    override fun onAttach(context: Context) {
        onInject()
        super.onAttach(context)
    }
}
