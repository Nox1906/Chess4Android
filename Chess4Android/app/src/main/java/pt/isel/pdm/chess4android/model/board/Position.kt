package pt.isel.pdm.chess4android.model.board

import pt.isel.pdm.chess4android.model.pieces.Piece

/**
 * Class Position will store board coordinates, png and piece if exists
 */
class Position(private val y: Int, private val x: Int, private val pngPosition: String) {

    private var piece: Piece? = null;

    fun getPiece(): Piece? {
        return this.piece;
    }

    fun setPiece(piece: Piece?) {
        this.piece = piece
    }

    fun getX(): Int {
        return this.x
    }

    fun getY(): Int {
        return this.y
    }

    fun getPng(): String {
        return pngPosition
    }
}