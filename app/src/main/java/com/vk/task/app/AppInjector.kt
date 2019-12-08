package com.vk.task.app

import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.vk.core.utils.extensions.optional
import com.vk.task.dependency.AppComponent
import com.vk.task.dependency.MainComponent
import com.vk.task.dependency.injector.InjectorPlugin
import com.vk.task.presentation.screens.main.MainActivity
import com.vk.task.presentation.screens.game.GameFragment


@MainThread
object AppInjector {

    // Primary data
    private lateinit var plugin: InjectorPlugin
    private lateinit var appComponent: AppComponent

    private val map = HashMap<LifecycleOwner, Any>()
    private val owner = LifecycleEventObserver { owner, event ->
        if (event == Lifecycle.Event.ON_DESTROY && map.containsKey(owner)) {
            map.remove(owner)
        }
    }

    private fun add(owner: LifecycleOwner, component: Any) {
        map[owner] = component
        owner.lifecycle.addObserver(AppInjector.owner)
    }

    fun init(appDelegate: AppDelegate, plugin: InjectorPlugin) {
        AppInjector.plugin = plugin

        val component = plugin.representAppComponent(appDelegate)
        component.inject(appDelegate)
        appComponent = component
    }

    fun injectActivity(activity: MainActivity, @IdRes layoutId: Int): MainComponent {
        val component = plugin.representMainComponent(appComponent, activity, layoutId)
        component.inject(activity)
        add(activity, component)
        return component
    }

    fun injectFragment(fragment: GameFragment) {
        map[fragment.requireActivity()] optional { data ->
            plugin
                .representComponent(data as MainComponent, fragment)
                .inject(fragment)
        } ?: throw IllegalStateException("MainComponent is null init him")
    }
}