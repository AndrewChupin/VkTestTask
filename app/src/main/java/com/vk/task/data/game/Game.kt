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

// Game
data class Game(
    val cards: List<GameCard>,
    val title: String,
    val leftShow: ShowInfo,
    val rightShow: ShowInfo
)

data class GameAnswer(
    val character: CharacterInfo,
    val isRightAnswer: Boolean,
    val answerShow: ShowInfo,
    val rightShow: ShowInfo
)

data class GameResult(
    val title: String,
    val answers: List<GameAnswer>,
    val totalPoints: Int,
    val earnedPoints: Int
)