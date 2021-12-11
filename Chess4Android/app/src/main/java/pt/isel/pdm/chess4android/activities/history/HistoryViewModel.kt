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
data class OriginalDailyPuzzle(
    var originalPuzzlePgn: String,
    var originalPuzzleSolution: MutableList<String>
) : Parcelable

class HistoryViewModel(application: Application) :
    AndroidViewModel(application) {
    var puzzleHistory: LiveData<List<DailyPuzzle>>? = null
        private set
    private val puzzleHistoryDao: PuzzleHistoryDao by lazy {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleHistoryDB.getHistoryDataBase()
    }
    var originalDailyPuzzle: OriginalDailyPuzzle? = null

    fun loadHistory(): LiveData<List<DailyPuzzle>> {
        val result = MutableLiveData<List<DailyPuzzle>>()
        puzzleHistory = result
        callbackAfterAsync(
            asyncAction = {
                puzzleHistoryDao.getAll().map {
                    val d = DailyPuzzle(
                        puzzleId = it.id,
                        puzzlePgn = it.pgn,
                        puzzleSolution = it.solution.split(",").toMutableList()
                    )
                    originalDailyPuzzle = OriginalDailyPuzzle(
                        it.originalPgn,
                        it.originalSolution.split(",").toMutableList()
                    )
                    d
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