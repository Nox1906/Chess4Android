package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position

class Queen(white: Boolean) : Piece(white) {

    private val type = Types.Q

    override fun getType(): Types {
        return type
    }

    override fun canMove(board: Board, start: Position, end: Position): Boolean {
        if (end.getPiece()?.isWhite() == this.isWhite()) {
            return false
        }

        return when {
            canMoveVertival(board, start, end) -> true
            canMoveHorizontal(board, start, end) -> true
            else -> canMoveDiagonal(board, start, end)
        }
    }
}