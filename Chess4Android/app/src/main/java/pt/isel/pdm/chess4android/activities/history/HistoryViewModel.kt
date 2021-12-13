package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import pt.isel.pdm.chess4android.common.*

@Parcelize
data class DailyPuzzleDbObject(
    var puzzleId: String,
    var puzzlePgn: String,
    var puzzleSolution: MutableList<String>,
    var originalPuzzlePgn: String,
    var originalPuzzleSolution: MutableList<String>
) : Parcelable

class HistoryViewModel(application: Application) :
    AndroidViewModel(application) {
    var puzzleHistory: LiveData<List<DailyPuzzleDbObject>>? = null
        private set
    private val puzzleHistoryDao: PuzzleHistoryDao by lazy {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleHistoryDB.getHistoryDataBase()
    }

    fun loadHistory(): LiveData<List<DailyPuzzleDbObject>> {
        val result = MutableLiveData<List<DailyPuzzleDbObject>>()
        puzzleHistory = result
        callbackAfterAsync(
            asyncAction = {
                puzzleHistoryDao.getAll().map {
                    DailyPuzzleDbObject(
                        puzzleId = it.id,
                        puzzlePgn = it.pgn,
                        puzzleSolution = it.solution.split(",").toMutableList(),
                        originalPuzzlePgn = it.originalPgn,
                        originalPuzzleSolution = it.originalSolution.split(",").toMutableList()
                    )
                }
            },
            callback = { res ->
                res.onSuccess { result.value = it }
                res.onFailure { result.value = emptyList() }
            },
        )
        return result
    }
}