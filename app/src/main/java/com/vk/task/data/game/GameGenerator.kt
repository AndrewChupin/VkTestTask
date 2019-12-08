package com.vk.task.data.game


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
            }.shuffled()
        )
    }
}
