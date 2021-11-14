package pt.isel.pdm.chess4android.model.board.moves

import pt.isel.pdm.chess4android.model.pieces.Piece
import pt.isel.pdm.chess4android.model.pieces.Types

class AttackMove( png: String, type: Types) : Move(png, type) {

    private var killedPiece: Piece? = null

    fun getKilledPiece(): Piece? {
        return this.killedPiece
    }

    fun setKilledPiece(kill: Piece) {
        this.killedPiece = kill
    }

    override fun toString(): String {
        return when {
            type == Types.P -> start?.getPng()!![0] + "x" + end?.getPng()
            disambiguating == null -> type.toString() + "x" + end?.getPng()
            else -> type.toString() + disambiguating + "x" + end?.getPng()
        }
    }
}