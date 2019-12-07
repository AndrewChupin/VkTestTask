package com.vk.core.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.vk.core.presentation.view.delegates.ActivityActionDelegate
import com.vk.core.presentation.view.delegates.ActivityActionHolder
import java.lang.ClassCastException


abstract class BaseFragment<V: View> : Fragment(), ActivityActionDelegate {

    companion object {
        private const val ARGUMENT_IS_FIRST_CREATE = "vk.core.BaseFragment.ARGUMENT_IS_FIRST_CREATE"
    }

    private var isFirstCreateView = true
    private var privateContent: V? = null
    protected val content: V get() {
        return privateContent ?: throw IllegalStateException("Content of this fragment isn't yet initialized or already destroyed")
    }

    abstract fun getContent(context: Context): Content<V>

    @CallSuper
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            isFirstCreateView = bundle.getBoolean(ARGUMENT_IS_FIRST_CREATE, true)
        }
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = container?.context ?: requireContext()
        return when (val content = getContent(context)) {
            is Content.Layout -> {
                privateContent = content.view

                privateContent // return
            }
            is Content.Resource -> {
                val view = inflater.inflate(content.layoutId, container, false)
                @Suppress("UNCHECKED_CAST")
                privateContent = view as? V ?: throw ClassCastException("If getContent() return Content.Resource you must specify generic type V of as View")

                view // return
            }
        }
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isFirstCreateView) {
            isFirstCreateView = false
            onFirstCreateView()
        }

        if (activity is ActivityActionHolder) {
            (activity as ActivityActionHolder).delegate = this
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(ARGUMENT_IS_FIRST_CREATE, isFirstCreateView)
        super.onSaveInstanceState(outState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        privateContent = null
        if (activity is ActivityActionHolder) {
            (activity as ActivityActionHolder).delegate = null
        }
    }

    // Return true if you don't want to execute onBackClick() inside Activity
    override fun onBackClick(): Boolean {
        return false
    }

    override fun onScreenResult(requestCode: Int, resultCode: Int, data: Intent?) {}

    protected open fun onFirstCreateView() {}

    protected fun <Data : Parcelable> getArgument(key: String): Data {
        arguments?.let {
            if (it.containsKey(key)) {
                it.getParcelable<Data>(key)?.let { data ->
                    return data
                }
            }
        }
        throw IllegalStateException("Data with key [$key] not found ")
    }
}
