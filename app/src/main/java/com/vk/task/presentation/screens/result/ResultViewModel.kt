package com.vk.task.presentation.screens.result

import com.vk.core.presentation.controller.DispatcherStateful
import com.vk.core.presentation.controller.StateController
import com.vk.core.presentation.controller.StateHolder
import com.vk.core.presentation.controller.ViewModelStateful
import com.vk.core.presentation.state.ViewProperty
import com.vk.core.presentation.state.ViewState
import com.vk.task.data.game.GameResult
import com.vk.task.domain.card.GameStore
import com.vk.task.presentation.screens.game.GameScreen
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// ViewState
data class ResultViewState(
    val game: ViewProperty<GameResult>
) : ViewState


// Dispatcher
interface ResultDispatcher : DispatcherStateful<ResultViewState> {
    fun tryAgain()
}


// ViewModel
class ResultViewModel(
    private val router: Router,
    private val cardStore: GameStore
) : ViewModelStateful<ResultViewState>(), ResultDispatcher, StateController<ResultViewState> {

    override val disposables: CompositeDisposable = CompositeDisposable()
    override val state: ResultViewState = createState()

    init {
        cardStore.getGameResult()
            .bindSubscribe(onSuccess = { result ->
                state.game.update(result)
            })
    }

    override fun tryAgain() {
        router.newRootChain(GameScreen())
    }

    override fun getStateHolder(): StateHolder<ResultViewState> {
        return this
    }

    override fun createState(): ResultViewState {
        return ResultViewState(
            game = ViewProperty.create(null)
        )
    }
}
