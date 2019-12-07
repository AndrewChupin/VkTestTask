package com.vk.task.dependency

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import com.vk.task.dependency.scope.ActivityScope
import com.vk.task.presentation.main.MainActivity
import com.vk.task.presentation.navigation.PlaneNavigator
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Navigator


@ActivityScope
@Subcomponent(
    modules = [
        MainModule::class
    ]
)
interface MainComponent {

    // Injects
    fun inject(activity: MainActivity)

    // Builders

    // Builder
    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun withActivity(fragmentActivity: FragmentActivity): Builder

        @BindsInstance
        fun withContainer(@IdRes containerId: Int): Builder

        fun build(): MainComponent
    }
}


@Module
class MainModule {
    @Provides
    @ActivityScope
    fun provideNavigator(mainNavigator: PlaneNavigator): Navigator = mainNavigator
}
