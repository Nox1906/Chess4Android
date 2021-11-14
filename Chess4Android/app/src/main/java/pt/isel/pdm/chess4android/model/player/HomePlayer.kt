package pt.isel.pdm.chess4android.model.player

class HomePlayer (override val whiteSide: Boolean) : Player() {
    override val humanPlayer: Boolean = true
}