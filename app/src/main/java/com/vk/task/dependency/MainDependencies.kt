package com.vk.task.dependency

import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import com.vk.task.data.game.GameRepository
import com.vk.task.data.game.GameRepositoryAssets
import com.vk.task.dependency.scope.ActivityScope
import com.vk.task.domain.card.GameStore
import com.vk.task.domain.card.GameStoreDefault
import com.vk.task.presentation.screens.main.MainActivity
import com.vk.task.presentation.navigation.PlaneNavigator
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Navigator


@ActivityScope
@Subcomponent(
    modules = [
        MainModule::class,
        GameModule::class
    ]
)
interface MainComponent {

    // Injects
    fun inject(activity: MainActivity)

    // Builders
    fun cardComponentBuilder(): CardComponent.Builder

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


@Module
class GameModule {

    @Provides
    @ActivityScope
    fun provideGameRepository(context: Context): GameRepository = GameRepositoryAssets(context)

    @Provides
    @ActivityScope
    fun provideGameStore(repository: GameRepository): GameStore = GameStoreDefault(repository)
}