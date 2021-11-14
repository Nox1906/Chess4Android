package pt.isel.pdm.chess4android.model.board.moves

import pt.isel.pdm.chess4android.model.board.Position
import pt.isel.pdm.chess4android.model.pieces.Piece
import pt.isel.pdm.chess4android.model.pieces.Types

/**
 * Abstract class of a move Type
 * Types of moves: Normal (Regular) ; Attack ; Castling ; Pawn Promotion TODO
 */
abstract class Move(
    val png: String,
    internal val type: Types
) {

    protected var start: Position? = null
    protected var end: Position? = null
    private var movedPiece: Piece? = null
    protected var disambiguating: Char? = null

    fun setDisambiguating(c: Char) {
        this.disambiguating = c
    }

    @JvmName("getStart1")
    fun getStart(): Position? {
        return start
    }

    @JvmName("getEnd1")
    fun getEnd(): Position? {
        return end
    }

    @JvmName("setStart1")
    fun setStart(position: Position) {
        this.movedPiece = position.getPiece()
        this.start = position
    }

    @JvmName("setEnd1")
    fun setEnd(end: Position) {
        this.end = end
    }

    /**
     * Check disambiguating moves
     */
    fun checkDisambiguating(): Boolean {
        return when {
            this.disambiguating == null -> true
            disambiguating!!.isLowerCase() -> start!!.getPng()[0] == disambiguating
            else -> start!!.getPng()[1] == disambiguating
        }
    }

    abstract override fun toString(): String
}