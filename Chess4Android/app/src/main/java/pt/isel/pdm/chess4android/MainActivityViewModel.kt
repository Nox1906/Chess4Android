package pt.isel.pdm.chess4android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import pt.isel.pdm.chess4android.model.board.Position
import pt.isel.pdm.chess4android.model.game.DailyGame

class MainActivityViewModel : ViewModel() {

    private val dailyPuzzleService: DailyPuzzleService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DailyPuzzleService::class.java)
    private var dailyGame : DailyGame? = null
    var resultOfDailyPuzzleObserver: MutableLiveData<ResultOfDailyPuzzle> = MutableLiveData()
    var resultOfDailyPuzzle: ResultOfDailyPuzzle? = null

    fun getDailyPuzzle() {
        dailyPuzzleService.getPuzzle().enqueue(object : Callback<ResultOfDailyPuzzle> {
            override fun onResponse(call: Call<ResultOfDailyPuzzle>, response: Response<ResultOfDailyPuzzle>) {
                resultOfDailyPuzzleObserver.value = response.body()
            }

            override fun onFailure(call: Call<ResultOfDailyPuzzle>, t: Throwable) {

            }
        })
    }
    fun setDailyGame(){
        dailyGame = resultOfDailyPuzzle?.game?.let { DailyGame(it.pgn,resultOfDailyPuzzle!!.puzzle.solution) }
    }
    fun getDailyGameBoard(): Array<Array<Position>>? {
        return dailyGame?.board?.getBoardPositions()
    }
    fun getDailyGame() : DailyGame? {
        return dailyGame
    }
}



