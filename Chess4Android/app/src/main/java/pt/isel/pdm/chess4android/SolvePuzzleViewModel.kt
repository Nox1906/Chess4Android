package pt.isel.pdm.chess4android

import android.provider.SyncStateContract
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData




class SolvePuzzleViewModel (private val state : SavedStateHandle) : ViewModel(){
    private val BOARD_KEY = "BOARD_KEY"
    var board : Array<Array<Pair<Army, Piece>>>? =null
    fun getBoard(board: Array<Array<Pair<Army, Piece>>>?): MutableLiveData<Array<Array<Pair<Army, Piece>>>?> {
        state.set(BOARD_KEY, board)
        return state.getLiveData(BOARD_KEY,board)
    }
//    fun saveBoard() {
//
//    }




}