package pt.isel.pdm.chess4android.model.pieces

import pt.isel.pdm.chess4android.model.board.Board
import pt.isel.pdm.chess4android.model.board.Position
import kotlin.math.abs

class Pawn(white: Boolean) : Piece(white) {

    private val type = Types.P

    override fun getType(): Types {
        return type
    }

    override fun canMove(board: Board, start: Position, end: Position): Boolean {

        if (end.getPiece()?.isWhite() == this.isWhite())
            return false;
        val y = end.getY() - start.getY()
        //First Move rule
        if (this.getFirstMove() && abs(y) == 2) {
            return this.canMoveVertival(board, start, end)
        }
        val x = end.getX() - start.getX()

        //Regular move
        if (y == 1 && !isWhite() || y == -1 && isWhite()) {
                if (x == 0)
                return true
            //Attacking Move
            else if (abs(x) == 1 && end.getPiece() != null) {
                return end.getPiece()?.isWhite() != this.isWhite()
            }
        }
        return false
    }
}