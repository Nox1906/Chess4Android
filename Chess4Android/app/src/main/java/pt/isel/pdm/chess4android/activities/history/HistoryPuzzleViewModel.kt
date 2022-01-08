package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.DailyPuzzle

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class HistoryPuzzleViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    val puzzleObserver: LiveData<DailyPuzzle> = state.getLiveData(ACTIVITY_VIEW_STATE)
    private val _error: MutableLiveData<Throwable> = MutableLiveData()

    fun getPuzzleFromDB() {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.asyncGetPuzzleByIdFromDB(
            puzzleObserver.value!!.getPuzzleId()
        ) { result ->
            result.onSuccess {
                val puzzle = it?.let { it1 ->
                    DailyPuzzle(
                        puzzleId = it1.id,
                        puzzlePgn = it.pgn,
                        puzzleSolution = it.solution.split(",").toMutableList()
                    )
                }
                state.set(ACTIVITY_VIEW_STATE, puzzle)
            }.onFailure {
            }
        }

    }

    fun saveCurrentStateInDB() {
        puzzleObserver.value?.let {
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

    fun getPuzzle(): DailyPuzzle? {
        return puzzleObserver.value
    }

    fun setPuzzle(puzzle: DailyPuzzle?) {
        state.set(ACTIVITY_VIEW_STATE, puzzle)
    }
}