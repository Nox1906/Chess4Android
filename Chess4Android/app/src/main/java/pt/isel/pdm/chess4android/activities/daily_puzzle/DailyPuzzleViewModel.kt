package pt.isel.pdm.chess4android.activities.daily_puzzle

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.*
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import pt.isel.pdm.chess4android.views.Tile

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class DailyPuzzleViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {


    val dailyPuzzle: LiveData<DailyPuzzle> = state.getLiveData(ACTIVITY_VIEW_STATE)
    private val _error: MutableLiveData<Throwable> = MutableLiveData()
    data class PreviousTile(val tile: Tile, val y: Int, val x: Int)

    var onMove: Boolean = false
    var previousTile: PreviousTile? = null


    fun fetchDailyPuzzle() {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.fetchDailyPuzzle(
            true,
            callback = { res ->
                res.onSuccess { state.set(ACTIVITY_VIEW_STATE, it) }
                res.onFailure { _error.value=it }
            })
    }

    private fun getDailyPuzzle(): DailyPuzzle? {
        return dailyPuzzle.value
    }


    fun solutionIsDone(): Boolean? {
        return dailyPuzzle.value?.getDailyGameStatus()
    }

    fun setDailyPuzzle(dailyPuzzle: DailyPuzzle) {
        state.set(ACTIVITY_VIEW_STATE, dailyPuzzle)
    }

    fun canMove(column: Int, row: Int): Boolean? {
        return getDailyPuzzle()?.playerMove(
            getDailyPuzzle()!!.currentPlayer,
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
        return getDailyPuzzle()!!.currentPlayer.isWhiteSide()
    }

    fun saveCurrentStateInDB() {
        dailyPuzzle.value?.let {
            getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.asyncUpdateToDB(
                it
            ) { saveToDbResult ->
                saveToDbResult.onSuccess {
                }.onFailure {
                    _error.value=Throwable("Failed to save in DB")
                }
            }
        }
    }
}



