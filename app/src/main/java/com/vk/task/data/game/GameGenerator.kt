package com.vk.task.data.game

import com.vk.core.common.Identical

// Character
data class CharacterInfo(
    override val id: Long,
    val image: String,
    val name: String,
    val showId: Long
) : Identical<Long>

// ShowInfo
data class ShowInfo(
    val id: Long,
    val name: String
)

// GameCard
data class GameCard(
    val character: CharacterInfo,
    val show: ShowInfo
)

// GameGenerator
data class GameGenerator(
    val chars: List<CharacterInfo>,
    val gameTitle: String,
    val leftShow: ShowInfo,
    val rightShow: ShowInfo
) {
    fun generate(): Game {
        return Game(
            leftShow = leftShow,
            rightShow = rightShow,
            title = gameTitle,
            cards = chars.map { char ->
                GameCard(
                    character = char,
                    show = when (char.showId == leftShow.id) {
                        true -> leftShow
                        else -> rightShow
                    }
                )
            }
        )
    }
}

data class Game(
    val cards: List<GameCard>,
    val title: String,
    val leftShow: ShowInfo,
    val rightShow: ShowInfo
)

// Answer
enum class AnswerType {
    RIGHT, WRONG
}

data class GameAnswer(
    val character: CharacterInfo,
    val answerType: AnswerType,
    val rightShow: ShowInfo,
    val wrongShow: ShowInfo?
)