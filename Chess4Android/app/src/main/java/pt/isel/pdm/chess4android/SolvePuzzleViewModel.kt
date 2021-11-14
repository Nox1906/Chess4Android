package pt.isel.pdm.chess4android

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class SolvePuzzleViewModel (private val state : SavedStateHandle) : ViewModel(){
    private val BOARD_KEY = "BOARD_KEY"
    var board:Array<Array<Pair<Army, Piece>>>?= null
    fun updateBoard(p:PieceAndItsPosition){
        board!![p.row][p.col]=p.pair
        setBoardState()
    }
    fun setBoardState(){
        state.set(BOARD_KEY, board)
    }
    fun getBoardState(): Array<Array<Pair<Army, Piece>>>?{
        return state.get(BOARD_KEY)
    }





}