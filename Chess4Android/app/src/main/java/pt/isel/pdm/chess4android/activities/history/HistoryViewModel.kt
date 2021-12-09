package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyGameChessApplication
import pt.isel.pdm.chess4android.model.game.DailyGame

class HistoryViewModel(application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {
    var puzzleHistory: LiveData<List<DailyGame>>? = null
        private set

    fun loadHistory(): LiveData<List<DailyGame>> {
        val puzzleDao =
            getApplication<DailyGameChessApplication>().dailyPuzzleHistoryDB.getHistoryDataBase()
        val puzzlesList = doAsyncWithResult { puzzleDao.getAll() }
        puzzleHistory = puzzleEntityToDailyGame(puzzlesList)
        return puzzlesList
    }


    fun puzzleEntityToDailyGame(puzzleEntityList: List<PuzzleEntity>): List<DailyGame> {
        return puzzleEntityList.map { createDailyGame(it) }
    }

    private fun createDailyGame(p: PuzzleEntity): DailyGame {

        val dg = DailyGame(p.id, p.pgn, p.solution.split(",").toMutableList())
        dg.board = p.board
        return dg
    }


}