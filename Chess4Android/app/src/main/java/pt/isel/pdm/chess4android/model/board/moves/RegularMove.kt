package pt.isel.pdm.chess4android.model.board.moves

import pt.isel.pdm.chess4android.model.pieces.Types

class RegularMove( png: String, type: Types) : Move( png, type) {

    override fun toString(): String {
        return when {
            type == Types.P && disambiguating == null -> end?.getPng()!!
            type != Types.P && disambiguating == null -> type.toString() + end?.getPng()
            else -> type.toString() + disambiguating + end?.getPng()
        }
    }
}