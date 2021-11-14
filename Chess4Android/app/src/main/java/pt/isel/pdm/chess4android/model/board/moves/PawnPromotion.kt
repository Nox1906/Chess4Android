package pt.isel.pdm.chess4android.model.board.moves

import pt.isel.pdm.chess4android.model.pieces.Types

class PawnPromotion(png: String, type: Types, private val replaceType : Types) : Move( png, type) {

    fun getReplaceType () : Types {
        return replaceType
    }

    override fun toString(): String {
        return end!!.getPng() + replaceType
    }


}