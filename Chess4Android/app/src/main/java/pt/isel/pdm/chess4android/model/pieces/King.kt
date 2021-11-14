package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position
import kotlin.math.abs

class King(white: Boolean) : Piece(white) {

    private var castlingDone = false;
    private val type = Types.K


    fun isCastlingDone(): Boolean {
        return this.castlingDone;
    }

    fun setCastlingDone(castlingDone: Boolean) {
        this.castlingDone = castlingDone;
    }

    override fun getType(): Types {
        return type
    }

    override fun canMove(board: Board, start: Position, end: Position): Boolean {

        if (end.getPiece()?.isWhite() == this.isWhite()) {
            return false
        }
        val x = abs(start.getX() - end.getX())
        val y = abs(start.getY() - end.getY())
        if (x + y == 1 || x == 1 && y == 1) {
            // TODO
            // check if this move will not result in the king
            // being attacked if so return true
            return true
        }
        return this.isValidCastling(board, start, end)
    }

    private fun isValidCastling(board: Board, start: Position, end: Position): Boolean {
        if (isCastlingDone() || !this.getFirstMove() || !canMoveHorizontal(board, start, end)) {
            return false
        }
        return true
    }

    fun isCastlingMove(end: Position): Boolean {
        if (!this.getFirstMove() && end.getX() == 6 || end.getY() == 2)
            return true
        return false
    }
}