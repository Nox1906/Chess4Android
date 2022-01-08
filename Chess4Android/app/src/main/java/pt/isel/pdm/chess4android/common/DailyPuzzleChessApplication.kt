package pt.isel.pdm.chess4android.common

import android.app.Application
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import pt.isel.pdm.chess4android.activities.daily_puzzle.DownloadDailyPuzzleWorker
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DailyPuzzleChessApplication : Application() {

    val dailyPuzzleService: DailyPuzzleService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }
    override fun onCreate() {
        super.onCreate()
        val workRequest = PeriodicWorkRequestBuilder<DownloadDailyPuzzleWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()

        WorkManager
            .getInstance(this)
            .enqueueUniquePeriodicWork(
                "DownloadDailyPuzzle",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }
    val dailyPuzzleHistoryDB by lazy {
        Room.databaseBuilder(this, PuzzleHistoryDataBase::class.java, "chessDB").fallbackToDestructiveMigration().build()
        //change to databaseBuilder()
    }
    val dailyPuzzleChessRepository by lazy {
        DailyPuzzleRepository(dailyPuzzleService, dailyPuzzleHistoryDB.getHistoryDataBase())
    }

    val regularGameRepository by lazy {
        RegularGameRepository(dailyPuzzleHistoryDB.getHistoryDataBase())
    }
}