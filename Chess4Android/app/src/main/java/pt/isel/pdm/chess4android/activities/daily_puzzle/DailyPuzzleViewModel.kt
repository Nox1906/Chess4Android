package pt.isel.pdm.chess4android.activities.daily_puzzle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import pt.isel.pdm.chess4android.model.game.DailyGame
import pt.isel.pdm.chess4android.views.Tile
import pt.isel.pdm.chess4android.common.*

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class DailyPuzzleViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {


    val dailyGame: LiveData<DailyGame> = state.getLiveData(ACTIVITY_VIEW_STATE)

    data class PreviousTile(val tile: Tile, val y: Int, val x: Int)

    var onMove: Boolean = false
    var previousTile: PreviousTile? = null
    var resultOfDailyPuzzle: ResultOfDailyPuzzle? = null

    fun getDailyPuzzle() {
        getApplication<DailyGameChessApplication>().dailyPuzzleService.getPuzzle()
            .enqueue(object : Callback<ResultOfDailyPuzzle> {
                override fun onResponse(
                    call: Call<ResultOfDailyPuzzle>,
                    response: Response<ResultOfDailyPuzzle>
                ) {
                    resultOfDailyPuzzle = response.body()
                    val dailyGame = resultOfDailyPuzzle?.puzzle?.solution?.let {
                        DailyGame(
                            resultOfDailyPuzzle!!.game.id,
                            resultOfDailyPuzzle!!.game.pgn,
                            it
                        )
                    }
                    state.set(ACTIVITY_VIEW_STATE, dailyGame)
                }

                override fun onFailure(call: Call<ResultOfDailyPuzzle>, t: Throwable) {

                }
            })
    }

    private fun getDailyGame(): DailyGame? {
        return dailyGame.value
    }

    fun dailyGameNotFetched(): Boolean {
        return dailyGame.value == null
    }

    fun solutionIsDone(): Boolean? {
        return dailyGame.value?.getDailyGameStatus()
    }

    fun canMove(column: Int, row: Int): Boolean? {
        return getDailyGame()?.playerMove(
            getDailyGame()!!.currentPlayer,
            previousTile!!.x,
            previousTile!!.y,
            column,
            row
        )
    }

    fun removeSolutionMove() {
        dailyGame.value?.removeSolutionMove()
    }


    fun isWhitePlayer(): Boolean {
        return getDailyGame()!!.currentPlayer.isWhiteSide()
    }

}



