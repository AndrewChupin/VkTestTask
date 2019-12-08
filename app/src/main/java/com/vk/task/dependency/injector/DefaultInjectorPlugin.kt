package com.vk.task.dependency.injector

import android.app.Application
import androidx.annotation.IdRes
import com.vk.task.dependency.*
import com.vk.task.presentation.screens.main.MainActivity
import com.vk.task.presentation.screens.game.GameFragment
import com.vk.task.presentation.screens.result.ResultFragment


object DefaultInjectorPlugin : InjectorPlugin {

    override fun representAppComponent(
        application: Application
    ): AppComponent = DaggerAppComponent.builder()
        .withApplication(application)
        .build()

    override fun representMainComponent(
        appComponent: AppComponent,
        activity: MainActivity,
        @IdRes layoutId: Int
    ): MainComponent = appComponent
        .mainComponentBuilder()
        .withActivity(activity)
        .withContainer(layoutId)
        .build()

    override fun representComponent(
        mainComponent: MainComponent,
        fragment: GameFragment
    ): GameComponent = mainComponent
        .gameComponentBuilder()
        .withFragment(fragment)
        .build()

    override fun representComponent(
        mainComponent: MainComponent,
        fragment: ResultFragment
    ): ResultComponent = mainComponent
        .resultComponentBuilder()
        .withFragment(fragment)
        .build()
}
