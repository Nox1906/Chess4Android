package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import pt.isel.pdm.chess4android.common.*

class HistoryViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    var puzzleHistory: LiveData<List<DailyPuzzle>>? = null
        private set
    private val puzzleHistoryDao: PuzzleHistoryDao by lazy {
        getApplication<DailyPuzzleChessApplication>().dailyPuzzleHistoryDB.getHistoryDataBase()
    }

    fun loadHistory(): LiveData<List<DailyPuzzle>> {
        val result = MutableLiveData<List<DailyPuzzle>>()
        puzzleHistory = result
        callbackAfterAsync(
            asyncAction = {
                puzzleHistoryDao.getAll().map {
                    DailyPuzzle(
                        puzzleId = it.id,
                        puzzlePgn = it.pgn,
                        puzzleSolution = it.solution.split(",").toMutableList()
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



//    private fun puzzleEntityToDailyGame(puzzleEntityList: List<PuzzleEntity>): List<DailyGame> {
//        return puzzleEntityList.map { createDailyGame(it) }
//    }
//
//    private fun createDailyGame(p: PuzzleEntity): DailyGame {
//
//        val dg = DailyGame(p.id, p.pgn, p.solution.split(",").toMutableList())
//        dg.board = p.board
//        return dg
//    }


}