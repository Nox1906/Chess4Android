package pt.isel.pdm.chess4android.activities.regular_game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.RegularGame
import pt.isel.pdm.chess4android.views.Tile

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class RegularGameViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {

    val regularGameObserver: LiveData<RegularGame> = state.getLiveData(ACTIVITY_VIEW_STATE)

    data class PreviousTile(val tile: Tile, val y: Int, val x: Int)

    var onMove: Boolean = false
    var previousTile: PreviousTile? = null

    private val _error: MutableLiveData<Throwable> = MutableLiveData()

    fun maybeGetRegularGameFromDB() {
        getApplication<DailyPuzzleChessApplication>().regularGameRepository.asyncGetRegularGameFromDb { result ->
            result.onSuccess {
                val regularGame: RegularGame?
                if (it != null) {
                    regularGame = RegularGame(it.pgn)
                    state.set(ACTIVITY_VIEW_STATE, regularGame)
                } else {
                    regularGame = RegularGame("")
                    saveRegularGameInDB(regularGame)
                }
            }.onFailure {
                _error.value = it
            }
        }

    }

    private fun saveRegularGameInDB(regularGame: RegularGame) {
        getApplication<DailyPuzzleChessApplication>().regularGameRepository.asyncSaveToDB(
            regularGame
        ) { saveToDbResult ->
            saveToDbResult.onSuccess {
                state.set(ACTIVITY_VIEW_STATE, regularGame)
            }.onFailure {
                _error.value = it
            }
        }
    }

    fun saveCurrentStateInDB() {
        regularGameObserver.value?.let {
            getApplication<DailyPuzzleChessApplication>().regularGameRepository.asyncUpdateToDB(
                it
            ) { saveToDbResult ->
                saveToDbResult.onSuccess {
                }.onFailure {
                    _error.value = Throwable("Failed to save in DB")
                }
            }
        }
    }

    private fun getRegularGame(): RegularGame? {
        return regularGameObserver.value
    }

    fun canMove(column: Int, row: Int): Boolean? {
        return getRegularGame()?.playerMove(
            getRegularGame()!!.currentPlayer,
            previousTile!!.x,
            previousTile!!.y,
            column,
            row
        )
    }

    fun isWhitePlayer(): Boolean {
        return getRegularGame()!!.currentPlayer.isWhiteSide()
    }
}
