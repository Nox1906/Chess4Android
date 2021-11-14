package pt.isel.pdm.chess4android

import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

data class ResultOfDailyPuzzle(val game: Game, val puzzle: Puzzle, var failureMessage: String?)

data class Game(
    val id: String,
    val pgn: String
    )
data class Puzzle(val id: String)

interface DailyPuzzleService {
    @GET("puzzle/daily")
    fun getPuzzle(): Call<ResultOfDailyPuzzle>
}
