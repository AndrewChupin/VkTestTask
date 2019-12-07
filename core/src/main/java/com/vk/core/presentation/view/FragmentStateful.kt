package com.vk.core.presentation.view

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.vk.core.common.Disposal
import com.vk.core.presentation.controller.DispatcherStateful
import com.vk.core.presentation.state.ViewProperty
import com.vk.core.presentation.state.ViewState
import com.vk.core.presentation.view.contracts.ViewWithRender
import com.vk.core.utils.extensions.optional

abstract class FragmentStateful<D : DispatcherStateful<VS>, VS : ViewState, V: View>
    : FragmentWithDispatcher<D, V>(), ViewWithRender<VS> {

    protected open val disposals = ArrayList<Disposal>()

    /**
     * Need for initialize view before create render channel. You might
     * use this method instead onViewCreated
     */
    abstract fun onViewCreatedBeforeRender(savedInstanceState: Bundle?)

    /**
     * Need for initialize other channel in the future
     */
    protected open fun onViewCreatedBeforeAttach(savedInstanceState: Bundle?) {}

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Render
        onViewCreatedBeforeRender(savedInstanceState)
        render(dispatcher.getStateHolder().state)

        // Attach
        onViewCreatedBeforeAttach(savedInstanceState)
        dispatcher.onAttach()
    }

    @CallSuper
    override fun onDestroyView() {
        dispatcher.onDetach()
        val iterator = disposals.iterator()
        while (iterator.hasNext()) {
            val disposal = iterator.next()
            if (!disposal.isDisposed()) {
                disposal.dispose()
            }
        }
        super.onDestroyView()
    }

    protected infix fun <Data> ViewProperty<Data>.bind(closure: (Data) -> Unit) {
        disposals.add(observe { data -> data optional closure })
    }

    protected infix fun <Data> ViewProperty<Data>.bindUnsafe(closure: (Data?) -> Unit) {
        disposals.add(observe(closure))
    }
}
