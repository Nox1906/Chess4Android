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
class DailyGame(private val puzzleInfo: String, private val puzzleSolution: Array<String>) : Game() {

    @IgnoredOnParcel
    private var moveCounter = 0

    /**
     * set pieces on board from a Portable Game Notation (PGN)
     */
    init {
        val pngCompiler = PngCompiler()
        pngCompiler.init()
        puzzleInfo.split("\\s".toRegex()).forEach { item ->
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
        if (move.getStart()?.getPng() + move.getEnd()?.getPng() != puzzleSolution[moveCounter]) {
            return false
        }
        moveCounter++
        return this.makeMove(move)
    }

    /**
     * will check if can make the move and do it
     * @return move succeeded
     */
    override fun makeMove(move: Move): Boolean {
        val sourcePiece = move.getStart()?.getPiece() ?: return false

        // valid move?
        if (!sourcePiece.canMove(board, move.getStart()!!, move.getEnd()!!))
            return false

        //Kill?
        if (move is AttackMove) {
            move.setKilledPiece(move.getEnd()!!.getPiece()!!)
            move.getKilledPiece()?.setIsKilled(true)
            board.removePiece(move.getKilledPiece()!!)
        }

        // castling?
        if (move is CastlingMove) {
            if (!move.getCastlePiece().getFirstMove()) return false
            board.changeRookOnCastling(move)
        }

        movesPlayed.add(move)
        if (sourcePiece.getFirstMove()) sourcePiece.setFirstMove()
        board.changePosition(sourcePiece, move.getStart(), move.getEnd())

        if (this.currentPlayer == players[0]) {
            this.currentPlayer = players[1];
        } else {
            this.currentPlayer = players[0];
        }
        return true;
    }
}