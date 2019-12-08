package com.vk.task.dependency

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vk.core.presentation.controller.ViewModelFactory
import com.vk.task.dependency.scope.FragmentScope
import com.vk.task.domain.card.GameStore
import com.vk.task.presentation.screens.result.ResultDispatcher
import com.vk.task.presentation.screens.result.ResultFragment
import com.vk.task.presentation.screens.result.ResultViewModel
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import ru.terrakok.cicerone.Router


@FragmentScope
@Subcomponent(
	modules = [
        ResultScreenModule::class
	]
)
interface ResultComponent {

	// Injects
	fun inject(fragment: ResultFragment)

	// Builder
	@Subcomponent.Builder
	interface Builder {

		@BindsInstance
		fun withFragment(fragment: Fragment): Builder

		fun build(): ResultComponent
	}
}


@Module
class ResultScreenModule {

	@Provides
	@FragmentScope
	fun provideViewModel(
		router: Router,
		gameStore: GameStore
	): ResultViewModel = ResultViewModel(router, gameStore)

	@Provides
	@FragmentScope
	fun provideDispatcher(
		fragment: Fragment,
		factory: ViewModelFactory<ResultViewModel>
	): ResultDispatcher = ViewModelProviders.of(fragment, factory).get(ResultViewModel::class.java)
}
