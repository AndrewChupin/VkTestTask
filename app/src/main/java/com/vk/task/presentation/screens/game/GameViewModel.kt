package com.vk.task.presentation.screens.game

import com.vk.core.presentation.controller.*
import com.vk.core.presentation.state.*
import com.vk.core.utils.extensions.optional
import com.vk.task.data.game.AnswerType
import com.vk.task.data.game.Game
import com.vk.task.data.game.GameAnswer
import com.vk.task.domain.card.GameStore
import com.vk.task.utils.DirectionType
import com.vk.task.utils.EMPTY_STR
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router


// ViewState
data class CardViewState(
    val game: ViewProperty<Game>
) : ViewState


// StateController
class CardStateController : StateController<CardViewState> {

    private val dirtyState = CardDirtyState(EMPTY_STR)
    override val state: CardViewState = createState()

    override fun createState(): CardViewState {
        return CardViewState(
            game = ViewProperty.create(null)
        )
    }

    private data class CardDirtyState(
        val countOf: String
    )
}


// Изменить API Card
// Нормальный проброс эвентов
// animItem при инизиализации листа, может быть подозрительно
// Архитектура
// Загрузка
// Попробовать снова при ошибке
// Время на ответ

// Dispatcher
interface CardDispatcher : DispatcherStateful<CardViewState> {
    fun swipedNextCard(direction: DirectionType)
}


// ViewModel
class CardViewModel(
    private val router: Router,
    private val cardStore: GameStore
) : ViewModelStateful<CardViewState>(), CardDispatcher, StateController<CardViewState> {

    override val disposables: CompositeDisposable = CompositeDisposable()
    override val state: CardViewState = createState()

    private var currentPosition = 0

    init {
        cardStore.createNewGame()
            .bindSubscribe(
                onSuccess = { game ->
                    currentPosition = game.cards.size - 1
                    state.game.update(game)
                })
    }

    override fun getStateHolder(): StateHolder<CardViewState> {
        return this
    }

    override fun createState(): CardViewState {
        return CardViewState(
            game = ViewProperty.create(null)
        )
    }

    override fun swipedNextCard(direction: DirectionType) {
        val position = currentPosition
        state.game.currentValue() optional { game ->
            val card = game.cards[position]

            val show = if (direction == DirectionType.LEFT) {
                game.leftShow
            } else {
                game.rightShow
            }

            if (card.character.showId == show.id) {
                cardStore.storeAnswer(
                    GameAnswer(
                        card.character,
                        AnswerType.RIGHT,
                        show,
                        null
                    )
                )
            } else {
                GameAnswer(
                    card.character,
                    AnswerType.WRONG,
                    show,
                    if (direction == DirectionType.LEFT) {
                        game.rightShow
                    } else {
                        game.leftShow
                    }
                )
            }
        }
        currentPosition--

        if (currentPosition < 0) {
            // router.navigateTo
        }
    }
}
