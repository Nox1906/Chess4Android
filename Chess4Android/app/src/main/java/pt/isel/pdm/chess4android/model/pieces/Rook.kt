package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position

class Rook(white: Boolean) : Piece(white) {

    private val type = Types.R

    override fun getType(): Types {
        return type
    }

    override fun canMove(board: Board, start: Position, end: Position): Boolean {
        if (end.getPiece()?.isWhite() == this.isWhite()) {
            return false
        }

        return if (canMoveHorizontal(board, start, end)) true
        else canMoveVertival(board, start, end)
    }
}