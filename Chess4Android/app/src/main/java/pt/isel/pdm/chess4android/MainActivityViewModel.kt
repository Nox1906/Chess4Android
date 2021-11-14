package pt.isel.pdm.chess4android

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivityViewModel : ViewModel() {
    private val side = 8
    private var dailyBoard = Array(side) {
        Array(side) { Pair(Army.EMPTY, Piece.EMPTY) }
    }
    private val dailyPuzzleService: DailyPuzzleService = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DailyPuzzleService::class.java)

    var resultOfDailyPuzzleObserver: MutableLiveData<ResultOfDailyPuzzle> = MutableLiveData()
    var resultOfDailyPuzzle: ResultOfDailyPuzzle? = null

    fun getDailyPuzzle() {
        dailyPuzzleService.getPuzzle().enqueue(object : Callback<ResultOfDailyPuzzle> {
            override fun onResponse(call: Call<ResultOfDailyPuzzle>, response: Response<ResultOfDailyPuzzle>) {
                resultOfDailyPuzzleObserver.value = response.body()
            }

            override fun onFailure(call: Call<ResultOfDailyPuzzle>, t: Throwable) {
                resultOfDailyPuzzleObserver.value?.failureMessage= t.message;
            }
        })
    }
    //: Array<Array<Pair<Army, Piece>>>?
    fun setDailyBoardMatrix(){
        Log.v("TAG PuzzleInfo", resultOfDailyPuzzle?.game?.pgn.toString())
        resultOfDailyPuzzle?.game?.pgn?.split("\\s".toRegex())?.forEach { item ->
            val pieceAndItsPosition = getPieceAndItsPosition(item)
            dailyBoard[pieceAndItsPosition.row][pieceAndItsPosition.col] = pieceAndItsPosition.pair
        }
    }
    fun getDailyBoardMatrix(): Array<Array<Pair<Army, Piece>>> {
        return dailyBoard
    }



    private fun getPieceAndItsPosition(pgn: String): PieceAndItsPosition {
        
        if (pgn.length == 2 && !pgn.contains('+') && !pgn.contains("-")) {
            return PieceAndItsPosition(
                (pgn[1].code - '0'.code) - 1, Columns.valueOf(
                    pgn[0].toString()
                        .uppercase(Locale.getDefault())
                ).ordinal, Pair(Army.WHITE, Piece.PAWN)
            )
        } else if (pgn.length == 3 && !pgn.contains('+')&& !pgn.contains("-")) {
            return PieceAndItsPosition(
                (pgn[2].code - '0'.code) - 1, Columns.valueOf(
                    pgn[1].toString()
                        .uppercase(Locale.getDefault())
                ).ordinal, Pair(Army.WHITE, getPiece(pgn[0]))
            )
        }
        //else if(pgn==4)
        return PieceAndItsPosition(0, 0, Pair(Army.BLACK, Piece.PAWN))
    }

    private fun getPiece(letter: Char): Piece {
        if (letter == 'B') return Piece.BISHOP
        if (letter == 'Q') return Piece.QUEEN
        if (letter == 'K') return Piece.KING
        if (letter == 'N') return Piece.KNIGHT
        return if (letter == 'R') Piece.ROOK
        else Piece.KING
    }

}



