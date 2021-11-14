package pt.isel.pdm.chess4android.model.player

abstract class Player {
    protected abstract val whiteSide : Boolean
    protected abstract val humanPlayer : Boolean

    fun isWhiteSide() : Boolean {
        return this.whiteSide
    }

    fun isHumanPlayer() : Boolean {
        return this.humanPlayer
    }
}