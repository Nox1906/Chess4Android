package pt.isel.pdm.chess4android.model.game.pngComp

import pt.isel.pdm.chess4android.model.pieces.Types
import pt.isel.pdm.chess4android.model.board.moves.*
import kotlin.collections.HashMap

/**
 * class to determinate move type and end position
 */
class PngCompiler() {

    val types = HashMap<Char, Types>()

     init {
        types['B'] = Types.B
        types['Q'] = Types.Q
        types['K'] = Types.K
        types['R'] = Types.R
        types['N'] = Types.N
    }

    /**
     * @return uncompleted move from a png.
     */
    fun getMove(png: String): Move? {
        val newPng = png.filter { it.isLetterOrDigit() }
        when {
            png == "" -> return null
            png == "O-O" -> {
                return CastlingMove(false, png, Types.K)
            }
            png == "O-O-O" -> {
                return CastlingMove(true, png, Types.K)
            }
            newPng[0].isLowerCase() -> {
                if (newPng.length >= 2 && !newPng.contains('x') && !newPng[newPng.lastIndex].isUpperCase())
                    return RegularMove(newPng, Types.P)
                else if (newPng.contains('x')) {
                    return AttackMove(newPng, Types.P)
                } else if (newPng[newPng.lastIndex].isUpperCase()) return PawnPromotion(
                    newPng, Types.P,
                    types[newPng[newPng.lastIndex]]!!
                )
            }
            newPng[0].isUpperCase() -> {
                val type: Types = types[newPng[0]]!!
                return if (!newPng.contains('x')) RegularMove(newPng, type)
                else AttackMove(newPng, type)
            }
        }
        return null
    }
}
