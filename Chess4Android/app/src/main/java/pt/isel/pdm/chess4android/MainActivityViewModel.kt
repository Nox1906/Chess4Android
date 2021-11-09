package pt.isel.pdm.chess4android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityViewModel : ViewModel() {
    enum class Army {
        WHITE, BLACK
    }

    enum class Piece {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING
    }
    enum class Columns{
        A,B,C,D,E,F,G,H
    }
    private val dailyPuzzleService: DailyPuzzleService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DailyPuzzleService::class.java)

        var puzzleInfo: MutableLiveData<PuzzleInfo> = MutableLiveData()

    fun getDailyBoard() {
        dailyPuzzleService.getPuzzle().enqueue(object : Callback<PuzzleInfo> {
            override fun onResponse(call: Call<PuzzleInfo>, response: Response<PuzzleInfo>) {
                puzzleInfo.value = response.body()
            }

            override fun onFailure(call: Call<PuzzleInfo>, t: Throwable) {
            }
        })
    }

}



