package com.vk.task.presentation.screens.result

import com.vk.core.presentation.controller.DispatcherStateful
import com.vk.core.presentation.controller.StateController
import com.vk.core.presentation.controller.StateHolder
import com.vk.core.presentation.controller.ViewModelStateful
import com.vk.core.presentation.state.ViewProperty
import com.vk.core.presentation.state.ViewState
import com.vk.core.utils.extensions.accept
import com.vk.core.utils.extensions.optional
import com.vk.task.data.game.Game
import com.vk.task.data.game.GameAnswer
import com.vk.task.data.game.GameResult
import com.vk.task.domain.card.GameStore
import com.vk.task.presentation.screens.game.GameScreen
import com.vk.task.utils.DirectionType
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// ViewState
data class ResultViewState(
    val result: ViewProperty<GameResult>
) : ViewState


// StateController
class ResultStateController : StateController<ResultViewState> {

    override val state: ResultViewState = createState()

    override fun createState(): ResultViewState {
        return ResultViewState(
            result = ViewProperty.create(null)
        )
    }

    fun showResult(result: GameResult) {
        state.result.update(result)
    }
}


// Dispatcher
interface ResultDispatcher : DispatcherStateful<ResultViewState> {
    fun tryAgain()
}


// ViewModel
class ResultViewModel(
    private val router: Router,
    private val cardStore: GameStore
) : ViewModelStateful<ResultViewState>(), ResultDispatcher {

    override val stateController = ResultStateController()
    override val disposables: CompositeDisposable = CompositeDisposable()

    init {
        cardStore.getGameResult()
            .bindSubscribe(onSuccess = { result ->
                stateController.showResult(result)
            })
    }

    override fun tryAgain() {
       cardStore.deleteResult()
           .bindSubscribe(onSuccess = {
               router.newRootChain(GameScreen())
           })
    }
}
