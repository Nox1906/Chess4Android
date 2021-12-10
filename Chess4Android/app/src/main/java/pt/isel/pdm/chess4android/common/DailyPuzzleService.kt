package pt.isel.pdm.chess4android.common

import retrofit2.Call
import retrofit2.http.GET

const val URL = "https://lichess.org/api/"

data class ResultOfDailyPuzzle(val game: Game, val puzzle: Puzzle)

data class Game(
    val id: String,
    val pgn: String
    )
data class Puzzle(
    val id: String,
    val solution: ArrayList<String>)

interface DailyPuzzleService {
    @GET("puzzle/daily")
    fun getPuzzle(): Call<ResultOfDailyPuzzle>
}
class ServiceUnavailable(message: String = "", cause: Throwable? = null) : Exception(message, cause)
