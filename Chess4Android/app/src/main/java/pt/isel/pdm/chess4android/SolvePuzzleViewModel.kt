package pt.isel.pdm.chess4android

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import pt.isel.pdm.chess4android.model.game.DailyGame
import pt.isel.pdm.chess4android.views.Tile


class SolvePuzzleViewModel (private val state : SavedStateHandle) : ViewModel(){
    private val BOARD_KEY = "BOARD_KEY"
    private var dailyGame : DailyGame? = null
//    fun updateBoard(p:PieceAndItsPosition){
//        board!![p.row][p.col]=p.pair
//        setDailyGameState()
//    }

    var previousTile : PreviousTile? = null
    fun setDailyGameState(){
        state.set(BOARD_KEY, dailyGame)
    }
    fun getDailyGameState(): DailyGame?{
        return state.get(BOARD_KEY)
    }
    fun getDailyGame() : DailyGame?{
        return dailyGame
    }
    fun setDailyGame(dailyGame: DailyGame){
        this.dailyGame=dailyGame
    }
    data class PreviousTile (val tile: Tile, val y:Int, val x :Int)





}