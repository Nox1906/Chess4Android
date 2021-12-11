package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"
class HistoryPuzzleViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    val puzzle: LiveData<DailyPuzzle> = state.getLiveData(ACTIVITY_VIEW_STATE)

    fun getPuzzleFromDB(id: String?){
        if (id != null) {
            getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.asyncGetPuzzleByIdFromDB(id) { result ->
                result.onSuccess {
                    val puzzle = it?.let { it1 -> DailyPuzzle(it1.id,it.pgn,it.solution.split(",").toMutableList()) }
                    state.set(ACTIVITY_VIEW_STATE,puzzle)
                }.onFailure {
                }
            }
        }
    }

    fun saveCurrentStateInDB() {
        puzzle.value?.let {
            getApplication<DailyPuzzleChessApplication>().dailyPuzzleChessRepository.asyncUpdateToDB(
                it
            ) { saveToDbResult ->
                saveToDbResult.onSuccess {
                }.onFailure {
                }
            }
        }
    }
    fun getPuzzle() : DailyPuzzle? {
        return puzzle.value
    }

    fun setPuzzle(puzzle: DailyPuzzle?){
        state.set(ACTIVITY_VIEW_STATE,puzzle)
    }
}