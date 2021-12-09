package pt.isel.pdm.chess4android.common

import android.app.Application
import android.content.res.Resources
import androidx.room.Room
import pt.isel.pdm.chess4android.activities.history.PuzzleHistoryDataBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DailyGameChessApplication : Application() {

    val dailyPuzzleService: DailyPuzzleService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }
    val dailyPuzzleHistoryDB by lazy{
        Room.inMemoryDatabaseBuilder(this, PuzzleHistoryDataBase::class.java).build()
    }

}