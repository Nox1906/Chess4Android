package pt.isel.pdm.chess4android.model.board.moves

import pt.isel.pdm.chess4android.model.board.Position
import pt.isel.pdm.chess4android.model.pieces.Piece
import pt.isel.pdm.chess4android.model.pieces.Types

class CastlingMove(private val isQueenSide: Boolean, png: String, type: Types) :
    Move(png, type) {

    private var castlePiece: Piece? = null

    fun getIsQueenSide(): Boolean {
        return isQueenSide
    }

    fun setCastlePiece(castlePos: Position) {
        this.castlePiece = castlePos.getPiece()
    }

    fun getCastlePiece(): Piece {
        return castlePiece!!
    }

    override fun toString(): String {
        return if (this.isQueenSide) "O-O-O" else "O-O"
    }
}