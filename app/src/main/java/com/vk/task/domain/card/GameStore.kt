package com.vk.task.domain.card

import com.vk.task.data.game.Game
import com.vk.task.data.game.GameAnswer
import com.vk.task.data.game.GameRepository
import com.vk.task.data.game.GameResult
import io.reactivex.Completable
import io.reactivex.Single


interface GameStore {
    fun createNewGame(): Single<Game>
    fun storeResult(answer: GameResult): Completable
    fun getGameResult(): Single<GameResult>
}

class GameStoreDefault(
    private val gameRepository: GameRepository
) : GameStore {

    override fun createNewGame(): Single<Game> = Single.defer {
        gameRepository.getGame()
    }

    override fun storeResult(result: GameResult): Completable = Completable.defer {
        gameRepository.saveGameResult(result)
    }

    override fun getGameResult(): Single<GameResult> = Single.defer {
        gameRepository.getGameResult()
    }
}
