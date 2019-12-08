package com.vk.task.data.game

import android.content.Context
import com.vk.core.utils.extensions.concat
import com.vk.task.utils.EMPTY_STR
import com.vk.task.utils.UrlUtils
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject


interface GameRepository {
    fun getGame(): Single<Game>
    fun getGameResult(): Single<GameResult>
    fun saveGameResult(gameResult: GameResult): Completable
}

class GameRepositoryAssets(
    private val context: Context
) : GameRepository {

    @Volatile
    private var gameGenerator: GameGenerator? = null

    @Volatile
    private var gameResult: GameResult? = null

    override fun getGame(): Single<Game> = Single.fromCallable {
        val generator = gameGenerator
        if (generator != null) {
            return@fromCallable generator.generate()
        }

        val gameData = loadGameGenerator()
        gameGenerator = gameData

        gameData.generate()
    }


    override fun saveGameResult(gameResult: GameResult): Completable = Completable.fromAction {
        this.gameResult = gameResult
    }

    override fun getGameResult(): Single<GameResult> = Single.fromCallable {
        gameResult
    }

    private fun loadGameGenerator(): GameGenerator {
        val gameStr = context.assets.open("game.json")
            .bufferedReader()
            .use { reader ->
                reader.readText()
            }

        val gameJson = JSONObject(gameStr)
        val gameTitle = gameJson.optString("game_title", EMPTY_STR)
        val shows = gameJson.getJSONArray("shows")

        check(shows.length() >= 2) {
            "We need at least 2 shows to start the game"
        }

        val leftShowJson = shows.getJSONObject(0)
        val rightShowJson = shows.getJSONObject(1)

        val leftShow = parseShow(leftShowJson)
        val rightShow = parseShow(rightShowJson)

        val chars = parseCharacters(leftShowJson, leftShow.id)
            .concat(parseCharacters(rightShowJson, rightShow.id))

        return GameGenerator(
            chars = chars,
            gameTitle = gameTitle,
            leftShow = leftShow,
            rightShow = rightShow
        )
    }

    private fun parseShow(jsonShow: JSONObject): ShowInfo {
        val id = jsonShow.getLong("id")
        val name = jsonShow.getString("name")

        return ShowInfo(id, name)
    }

    private fun parseCharacters(jsonShow: JSONObject, showId: Long): List<CharacterInfo> {
        val charactersJson = jsonShow.getJSONArray("characters")

        val characters = mutableListOf<CharacterInfo>()
        for (i in 0 until charactersJson.length()) {
            characters.add(parseCharacter(charactersJson.getJSONObject(i), showId))
        }

        return characters
    }

    private fun parseCharacter(jsonCharacter: JSONObject, showId: Long): CharacterInfo {
        val id = jsonCharacter.getLong("id")
        val name = jsonCharacter.getString("name")
        val image = jsonCharacter.getString("image")

        return CharacterInfo(id, UrlUtils.getAssetsUrl(image), name, showId)
    }


}