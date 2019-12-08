package com.vk.task.dependency.injector

import android.app.Application
import androidx.annotation.IdRes
import com.vk.task.dependency.AppComponent
import com.vk.task.dependency.GameComponent
import com.vk.task.dependency.MainComponent
import com.vk.task.dependency.ResultComponent
import com.vk.task.presentation.screens.main.MainActivity
import com.vk.task.presentation.screens.game.GameFragment
import com.vk.task.presentation.screens.result.ResultFragment


interface InjectorPlugin {

    fun representAppComponent(
        application: Application
    ): AppComponent

    fun representMainComponent(
        appComponent: AppComponent,
        activity: MainActivity,
        @IdRes layoutId: Int
    ): MainComponent

    fun representComponent(
        mainComponent: MainComponent,
        fragment: GameFragment
    ): GameComponent

    fun representComponent(
        mainComponent: MainComponent,
        fragment: ResultFragment
    ): ResultComponent
}
