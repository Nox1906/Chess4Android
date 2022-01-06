package pt.isel.pdm.chess4android.model.game

import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.model.player.Player

@Parcelize
class RegularGame : Game() {
    init {

    }

    override fun playerMove(
        player: Player,
        startX: Int,
        startY: Int,
        endX: Int,
        endY: Int
    ): Boolean {
        if (player.isWhiteSide() != currentPlayer.isWhiteSide()) return false
        val move = board.getPossibleMove(player, startX, startY, endX, endY) ?: return false
        return this.makeMove(move)
    }
}