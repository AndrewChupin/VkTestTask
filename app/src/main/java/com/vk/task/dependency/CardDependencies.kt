package com.vk.task.dependency

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vk.core.presentation.controller.ViewModelFactory
import com.vk.task.dependency.scope.FragmentScope
import com.vk.task.domain.card.GameStore
import com.vk.task.presentation.screens.game.GameDispatcher
import com.vk.task.presentation.screens.game.GameFragment
import com.vk.task.presentation.screens.game.GameViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(
	modules = [
		CardScreenModule::class
	]
)
interface GameComponent {

	// Injects
	fun inject(fragment: GameFragment)

	// Builder
	@Subcomponent.Builder
	interface Builder {

		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): GameComponent
	}
}


@Module
class CardScreenModule {

	@Provides
	@FragmentScope
	fun provideViewModel(
		router: Router,
		gameStore: GameStore
	): GameViewModel = GameViewModel(router, gameStore)

	@Provides
	@FragmentScope
	fun provideDispatcher(
		fragment: Fragment,
		factory: ViewModelFactory<GameViewModel>
	): GameDispatcher = ViewModelProviders.of(fragment, factory).get(GameViewModel::class.java)
}
