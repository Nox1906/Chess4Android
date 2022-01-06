package pt.isel.pdm.chess4android.activities.regular_game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
import pt.isel.pdm.chess4android.model.game.RegularGame
import pt.isel.pdm.chess4android.views.Tile

private const val ACTIVITY_VIEW_STATE = "Activity.ViewState"

class RegularGameViewModel (application: Application, private val state: SavedStateHandle) :
    AndroidViewModel(application) {

    val regularGame: LiveData<RegularGame> = state.getLiveData(ACTIVITY_VIEW_STATE)

    data class PreviousTile(val tile: Tile, val y: Int, val x: Int)

    var onMove: Boolean = false
    var previousTile: PreviousTile? = null


    private fun getRegularGame(): RegularGame? {
        return regularGame.value
    }

    fun setRegularGame(regularGame: RegularGame) {
        state.set(pt.isel.pdm.chess4android.activities.regular_game.ACTIVITY_VIEW_STATE, regularGame)
    }

    fun canMove(column: Int, row: Int): Boolean? {

        return getRegularGame()?.playerMove(
            getRegularGame()!!.currentPlayer,
            previousTile!!.x,
            previousTile!!.y,
            column,
            row
        )
    }

    fun isWhitePlayer(): Boolean {
        return getRegularGame()!!.currentPlayer.isWhiteSide()
    }
}
