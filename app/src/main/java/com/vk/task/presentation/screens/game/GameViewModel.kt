package com.vk.task.presentation.screens.game

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
import com.vk.task.presentation.screens.result.ResultScreen
import com.vk.task.utils.DirectionType
import com.vk.task.utils.EMPTY_STR
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// ViewState
data class GameViewState(
    val game: ViewProperty<Game>,
    val isLoading: ViewProperty<Boolean>
) : ViewState


// StateController
class GameStateController : StateController<GameViewState> {

    private val dirtyState = GameDirtyState()
    override val state: GameViewState = createState()

    override fun createState(): GameViewState {
        return GameViewState(
            game = ViewProperty.create(null),
            isLoading = ViewProperty.create(true)
        )
    }

    fun startTheGame(newGame: Game) {
        dirtyState.currentPosition = 0

        state accept {
            game.update(newGame)
            isLoading.update(false)
        }
    }

    fun countSwipe(direction: DirectionType): GameResult? {
        dirtyState accept {
            state.game.currentValue() optional { game ->
                val card = game.cards[currentPosition]

                val answerShow = when (direction == DirectionType.LEFT) {
                    true -> game.leftShow
                    else -> game.rightShow
                }

                val isRightAnswer = answerShow.id == card.show.id
                answers.add(GameAnswer(
                    card.character,
                    isRightAnswer,
                    answerShow,
                    card.show
                ))

                currentPosition++

                if (currentPosition == game.cards.size) {
                    return GameResult(
                        title = game.title,
                        answers = answers.reversed(),
                        totalPoints = answers.size,
                        earnedPoints = answers.count(GameAnswer::isRightAnswer)
                    )
                }
            }
        }

        return null
    }

    private data class GameDirtyState(
        var answers: MutableList<GameAnswer> = mutableListOf(),
        var currentPosition: Int = 0
    )
}


// Dispatcher
interface GameDispatcher : DispatcherStateful<GameViewState> {
    fun swipedNext(direction: DirectionType)
}


// ViewModel
class GameViewModel(
    private val router: Router,
    private val cardStore: GameStore
) : ViewModelStateful<GameViewState>(), GameDispatcher {

    override val stateController = GameStateController()
    override val disposables: CompositeDisposable = CompositeDisposable()

    init {
        cardStore.createNewGame()
            .bindSubscribe(onSuccess = { newGame ->
                stateController.startTheGame(newGame)
            })
    }

    // TODO-2: This signature looks really weird i have to change it, but at first i have to change Swipeable API
    override fun swipedNext(direction: DirectionType) {
        stateController.countSwipe(direction) optional { result ->
            cardStore.storeResult(result)
                .bindSubscribe(onSuccess = {
                    router.navigateTo(ResultScreen())
                })
        }
    }
}
