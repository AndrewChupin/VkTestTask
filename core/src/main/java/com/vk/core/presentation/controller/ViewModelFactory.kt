package com.vk.core.presentation.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Lazy
import javax.inject.Inject

/**
 * Universal ViewModelFactory
 * This class using specifically for [dagger]
 * It lets you to keep single [ViewModelFactory] and you don't have to create them for each
 * new [ViewModel]
 * </br>
 * In addition, this class uses [Lazy] parameter that means:
 * 1) Don't create [ViewModel] until inject() invocation
 * 2) Don't recreate ViewModel with LifecycleOwner(for example recreate activity)
 *
 * @author Andrew Chupin
 */
class ViewModelFactory<VM : ViewModel> @Inject constructor(
    private val viewModel: Lazy<@JvmSuppressWildcards VM>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val vm = viewModel.get()
        if (modelClass.isAssignableFrom(vm.javaClass)) {
            @Suppress("UNCHECKED_CAST")
            return vm as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class ${viewModel.javaClass.canonicalName} need ${modelClass.canonicalName}"
        )
    }
}
