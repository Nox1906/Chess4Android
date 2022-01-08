package pt.isel.pdm.chess4android.activities.daily_puzzle

import android.content.Context
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.common.DailyPuzzleRepository

class DownloadDailyPuzzleWorker(appContext: Context, workerParams: WorkerParameters)
: ListenableWorker(appContext, workerParams) {
    override fun startWork(): ListenableFuture<Result> {
        val app : DailyPuzzleChessApplication = applicationContext as DailyPuzzleChessApplication
        val repo = DailyPuzzleRepository(app.dailyPuzzleService, app.dailyPuzzleHistoryDB.getHistoryDataBase())

        return CallbackToFutureAdapter.getFuture { completer ->
            repo.fetchDailyPuzzle(mustSaveToDB = true) { result ->
                result
                    .onSuccess {
                        completer.set(Result.success())
                    }
                    .onFailure {
                        completer.setException(it)
                    }
            }
        }
    }
}