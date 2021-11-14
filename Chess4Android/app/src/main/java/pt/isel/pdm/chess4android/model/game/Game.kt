package pt.isel.pdm.chess4android.model.game

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.moves.Move
import pt.isel.pdm.chess4android.model.player.AwayPlayer
import pt.isel.pdm.chess4android.model.player.HomePlayer
import pt.isel.pdm.chess4android.model.player.Player
import java.util.*

/**
 * Abstract class of all games
 */
abstract class Game {

    protected var movesPlayed: LinkedList<Move> = LinkedList()
    val board: Board = Board()
    protected var players: Array<Player> = arrayOf(HomePlayer(whiteSide = true), AwayPlayer(whiteSide = false))
    protected var currentPlayer: Player = players[0]

    abstract fun init()

    abstract fun playerMove(player: Player, startX: Int, startY: Int, endX: Int, endY: Int): Boolean

    protected abstract fun makeMove(move: Move): Boolean
}