package pt.isel.pdm.chess4android.common

import android.app.Application
import android.content.res.Resources
import android.util.Log
import androidx.room.Room
import pt.isel.pdm.chess4android.activities.history.PuzzleEntity
import pt.isel.pdm.chess4android.activities.history.PuzzleHistoryDataBase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DailyPuzzleChessApplication : Application() {

    private val dailyPuzzleService: DailyPuzzleService by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DailyPuzzleService::class.java)
    }
    val dailyPuzzleHistoryDB by lazy {
        Room.inMemoryDatabaseBuilder(this, PuzzleHistoryDataBase::class.java).build()
        //change to databaseBuilder()
    }
    val dailyPuzzleChessRepository by lazy {
        DailyPuzzleChessRepository(dailyPuzzleService, dailyPuzzleHistoryDB.getHistoryDataBase())
    }

//    override fun onCreate() {
//        super.onCreate()
//        callbackAfterAsync({ }) {
//            dailyPuzzleHistoryDB.getHistoryDataBase().insert(
//                PuzzleEntity(
//                    id = "U2wRAGkr", pgn = "e4 c5 Nf3 Nc6 Bc4 e6 d3 g6 Bg5 Qb6 Qc1 Bg7 Nc3 Na5 O-O Nxc4 dxc4 Bxc3 bxc3 f6 Bh6 Nxh6 Qxh6 Qd6 Nh4 Qf8 Qe3 b6 e5 f5 Qf3 Rb8 Rfd1 Bb7 Qd3 Bc6 Nf3 Qg7 a4 O-O a5 Ra8 h3 Rfd8 axb6 axb6 Rxa8 Rxa8 Rb1 Ra2 Rxb6 Qh6 Rb8+ Kf7 Qd6 Ra1+ Kh2",
//                    solution = arrayListOf("h6f4", "g2g3", "a1h1", "h2h1", "f4f3", "h1g1", "f3g2").joinToString(separator = ","))
//            )
//        }
//    }
    //    var d: DailyGame = DailyGame(
//        puzzleId = "U2wRAGkr",
//        puzzlePgn = "e4 c5 Nf3 Nc6 Bc4 e6 d3 g6 Bg5 Qb6 Qc1 Bg7 Nc3 Na5 O-O Nxc4 dxc4 Bxc3 bxc3 f6 Bh6 Nxh6 Qxh6 Qd6 Nh4 Qf8 Qe3 b6 e5 f5 Qf3 Rb8 Rfd1 Bb7 Qd3 Bc6 Nf3 Qg7 a4 O-O a5 Ra8 h3 Rfd8 axb6 axb6 Rxa8 Rxa8 Rb1 Ra2 Rxb6 Qh6 Rb8+ Kf7 Qd6 Ra1+ Kh2",
//        puzzleSolution = arrayListOf("h6f4", "g2g3", "a1h1", "h2h1", "f4f3", "h1g1", "f3g2")
//    )

}