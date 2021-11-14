package pt.isel.pdm.chess4android.model.player

class AwayPlayer (override val whiteSide: Boolean) : Player() {
    override val humanPlayer: Boolean = false
}