package pt.isel.pdm.chess4android.model.game

import android.os.Parcelable
import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.moves.AttackMove
import pt.isel.pdm.chess4android.model.board.moves.CastlingMove
import pt.isel.pdm.chess4android.model.board.moves.Move
import pt.isel.pdm.chess4android.model.player.AwayPlayer
import pt.isel.pdm.chess4android.model.player.HomePlayer
import pt.isel.pdm.chess4android.model.player.Player
import java.io.File.separator
import java.util.*

/**
 * Abstract class of all games*/

abstract class Game : Parcelable {
//    var originalSolution: MutableList<String>? = null
//    var originalPgn: String? = null
    protected var movesPlayed: LinkedList<Move> = LinkedList()
    var board: Board = Board()
    protected var players: Array<Player> =
        arrayOf(HomePlayer(whiteSide = true), AwayPlayer(whiteSide = false))
    var currentPlayer: Player = players[0]

    //abstract fun init()

    abstract fun playerMove(player: Player, startX: Int, startY: Int, endX: Int, endY: Int): Boolean

    fun getMovesPlayedAsPgn(): String {

        return movesPlayed.joinToString(separator = " ")
    }

    /**
     * will check if can make the move and do it
     * @return move succeeded
     */
    fun makeMove(move: Move): Boolean {
        val sourcePiece = move.getStart()?.getPiece() ?: return false

        // valid move?
        if (!sourcePiece.canMove(board, move.getStart()!!, move.getEnd()!!))
            return false

        //Kill?
        if (move is AttackMove) {
            move.setKilledPiece(move.getEnd()!!.getPiece()!!)
            move.getKilledPiece()?.setIsKilled(true)
            board.removePiece(move.getKilledPiece()!!)
        }

        // castling?
        if (move is CastlingMove) {
            if (!move.getCastlePiece().getFirstMove()) return false
            board.changeRookOnCastling(move)
        }

        movesPlayed.add(move)
        if (sourcePiece.getFirstMove()) sourcePiece.setFirstMove()
        board.changePosition(sourcePiece, move.getStart(), move.getEnd())

        if (this.currentPlayer == players[0]) {
            this.currentPlayer = players[1];
        } else {
            this.currentPlayer = players[0];
        }
        return true
    }
}