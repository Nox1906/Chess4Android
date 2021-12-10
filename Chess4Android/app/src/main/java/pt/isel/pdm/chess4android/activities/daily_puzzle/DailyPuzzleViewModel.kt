package pt.isel.pdm.chess4android.activities.daily_puzzle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import pt.isel.pdm.chess4android.views.Tile
import pt.isel.pdm.chess4android.common.*

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class DailyPuzzleViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {


    val dailyPuzzle: LiveData<DailyPuzzle> = state.getLiveData(ACTIVITY_VIEW_STATE)

    data class PreviousTile(val tile: Tile, val y: Int, val x: Int)

    var onMove: Boolean = false
    var previousTile: PreviousTile? = null
    //var resultOfDailyPuzzle: ResultOfDailyPuzzle? = null

    fun getDailyPuzzle() {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.fetchDailyPuzzle(
            "adsd",
            true,
            callback = { res ->
                res.onSuccess { state.set(ACTIVITY_VIEW_STATE, it) }
                res.onFailure { state.set(ACTIVITY_VIEW_STATE, null) }
            })
    }


    private fun getDailyGame(): DailyPuzzle? {
        return dailyPuzzle.value
    }

    fun dailyGameNotFetched(): Boolean {
        return dailyPuzzle.value == null
    }

    fun solutionIsDone(): Boolean? {
        return dailyPuzzle.value?.getDailyGameStatus()
    }

    fun setDailyGame(dailyPuzzle: DailyPuzzle) {
        state.set(ACTIVITY_VIEW_STATE, dailyPuzzle)
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
        dailyPuzzle.value?.removeSolutionMove()
    }


    fun isWhitePlayer(): Boolean {
        return getDailyGame()!!.currentPlayer.isWhiteSide()
    }

}



