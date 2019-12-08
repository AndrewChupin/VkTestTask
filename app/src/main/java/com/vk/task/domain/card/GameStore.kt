package com.vk.task.domain.card

import com.vk.task.data.game.Game
import com.vk.task.data.game.GameAnswer
import com.vk.task.data.game.GameRepository
import io.reactivex.Single


interface GameStore {
    fun createNewGame(): Single<Game>
    fun storeAnswer(answer: GameAnswer)
}

class GameStoreDefault(
    private val gameRepository: GameRepository
) : GameStore {

    override fun createNewGame(): Single<Game> = Single.defer {
        gameRepository.getGame()
    }

    override fun storeAnswer(answer: GameAnswer) = gameRepository.saveGameAnswer(answer)
}
