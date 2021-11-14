package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position
import kotlin.math.abs

/**
 * abstract class with all common parameters of chess pieces
 * Pieces types: King, Queen, Bishop, Knight, Rook and Pawn
 */
abstract class Piece(private var white: Boolean) {

    private var killed = false
    private var firstMove = true

    fun isWhite(): Boolean {
        return this.white
    }

    fun setIsKilled(killed: Boolean) {
        this.killed = killed;
    }

    abstract fun getType(): Types;

    /**
     * will validate moves rules
     */
    abstract fun canMove(board: Board, start: Position, end: Position): Boolean

    /**
     * will check middle pieces and if can make the move on Vertical
     */
    protected fun canMoveVertival(board: Board, start: Position, end: Position): Boolean {
        val x = end.getX() - start.getX()
        val y = end.getY() - start.getY()
        //Vertical move
        if (abs(x) == 0 && abs(y) != 0) {
            val dirY = y / abs(y)
            for (i in 1 until abs(y)) {
                if (board.getPosition(start.getY() + (i * dirY), start.getX()).getPiece() == null)
                    continue
                else return false
            }
            return true
        }
        return false
    }

    /**
     * will check middle pieces and if can make the move on Horizontal
     */
    protected fun canMoveHorizontal(board: Board, start: Position, end: Position): Boolean {
        val x = end.getX() - start.getX()
        val y = end.getY() - start.getY()
        if (abs(x) != 0 && abs(y) == 0) {
            val dirX = x / abs(x)
            for (i in 1 until abs(x)) {
                if (board.getPosition(start.getY(), start.getX() + (i * dirX)).getPiece() == null)
                    continue
                else return false
            }
            return true
        }
        return false
    }

    /**
     * will check middle pieces and if can make the move on Diagonal
     */
    protected fun canMoveDiagonal(board: Board, start: Position, end: Position): Boolean {
        val x = end.getX() - start.getX()
        val y = end.getY() - start.getY()
        if (abs(x) == abs(y)) {
            val dirX = x / abs(x)
            val dirY = y / abs(y)
            for (i in 1 until abs(x)) {
                if (board.getPosition(start.getY() + (i * dirY), start.getX() + (i * dirX)).getPiece() == null)
                    continue
                else return false
            }
            return true
        }
        return false;
    }

    /**
     * will check if is first move (Important to Pawns, Kings and Rooks
     */
    fun setFirstMove() {
        if (firstMove)
            firstMove = !firstMove
    }

    fun getFirstMove(): Boolean {
        return firstMove
    }
}