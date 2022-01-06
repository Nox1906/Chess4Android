package pt.isel.pdm.chess4android.model.game

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pt.isel.pdm.chess4android.model.board.moves.AttackMove
import pt.isel.pdm.chess4android.model.board.moves.CastlingMove
import pt.isel.pdm.chess4android.model.board.moves.Move
import pt.isel.pdm.chess4android.model.game.pngComp.PngCompiler
import pt.isel.pdm.chess4android.model.player.Player

/**
 * Class with daily chess puzzles!
 */
@Parcelize
class DailyPuzzle(private val puzzleId : String, private var puzzlePgn: String, private var puzzleSolution: MutableList<String>) : Game() {

    /**
     * set pieces on board from a Portable Game Notation (PGN)
     */
    init {
        val pngCompiler = PngCompiler()
        puzzlePgn.split("\\s".toRegex()).forEach { item ->
            val move: Move? = pngCompiler.getMove(item)
            if (move != null){
                board.setDailyPositions(currentPlayer, move)
            }
            for (piece in board.getPieces(currentPlayer.isWhiteSide())) {
                if (piece.getType() != move?.type) continue
                move.setStart(board.getPiecePosition(piece))
                if (!move.checkDisambiguating()) continue
                if (makeMove(move)) break
            }
        }
    }


    /**
     * will create a new move
     * @return if move matches with solution
     */
    override fun playerMove(player: Player, startX: Int, startY: Int, endX: Int, endY: Int): Boolean {
        if (player.isWhiteSide() != currentPlayer.isWhiteSide()) return false
        val move = board.getPossibleMove(player, startX, startY, endX, endY) ?: return false
        if (move.getStart()?.getPng() + move.getEnd()?.getPng() != puzzleSolution[0]) {
            return false
        }
        return this.makeMove(move)
    }


    fun getPuzzleSolution() : MutableList<String> = this.puzzleSolution

    fun getDailyGameStatus() : Boolean{
        return puzzleSolution.isEmpty() || puzzleSolution[0]==""
    }
    fun getPuzzleId() : String = this.puzzleId

    fun removeSolutionMove(){
        puzzleSolution.removeAt(0)
    }
    fun getPgn(): String = this.puzzlePgn

    fun setPgn(pgn : String?){
        if (pgn != null) {
            this.puzzlePgn= pgn
        }
    }
    fun setSolution(solution : MutableList<String>?){
        if (solution != null) {
            this.puzzleSolution= solution
        }
    }

}