package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position
import kotlin.math.abs


class Knight(white: Boolean) : Piece(white) {

    private val type = Types.N

    override fun getType(): Types {
        return type
    }

    override fun canMove(board: Board, start: Position, end: Position): Boolean {
        if (end.getPiece()?.isWhite() == isWhite()) {
            return false
        }
        val x = abs(start.getX() - end.getX())
        val y = abs(start.getY() - end.getY())
        return x * y == 2
    }
}