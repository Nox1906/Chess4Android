package pt.isel.pdm.chess4android.model.board

import pt.isel.pdm.chess4android.model.pieces.*
import pt.isel.pdm.chess4android.model.player.Player
import pt.isel.pdm.chess4android.model.board.moves.AttackMove
import pt.isel.pdm.chess4android.model.board.moves.CastlingMove
import pt.isel.pdm.chess4android.model.board.moves.Move
import pt.isel.pdm.chess4android.model.board.moves.RegularMove
import pt.isel.pdm.chess4android.model.pieces.*
import kotlin.math.abs
import java.util.*

/**
 * Class to store all positions and pieces on a Board.
 */
class Board() {

    private val COLUMNS = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h')
    private val LINES = arrayOf('8', '7', '6', '5', '4', '3', '2', '1')
    private val whitePieces = LinkedList<Piece>()
    private val blackPieces = LinkedList<Piece>()
    private val blackKing = King(false)
    private val whiteKing = King(true)
    private val piecesPositions = hashMapOf<Piece, Position>()
    private val pngMap = hashMapOf<String, Position?>()
    private val positions = Array(LINES.size) { row ->
        Array(COLUMNS.size) { col ->
            Position(row, col, COLUMNS[col] + "" + LINES[row])
        }
    }

    /**
     * Create Array of positions
     */
    init {
        Array(LINES.size) { row ->
            Array(COLUMNS.size) { col ->
                pngMap.put(COLUMNS[col] + "" + LINES[row], positions[row][col])
            }
        }
        resetBoard()
    }

    /**
     * Reset pieces into initial game
     */
    fun resetBoard() {
        Array(positions.size) { col ->
            positions[1][col].setPiece(Pawn(false))
            blackPieces.add(positions[1][col].getPiece()!!)
            piecesPositions.put(positions[1][col].getPiece()!!, getPosition(1, col))
            positions[6][col].setPiece(Pawn(true))
            whitePieces.add(positions[6][col].getPiece()!!)
            piecesPositions.put(positions[6][col].getPiece()!!, getPosition(6, col))
        }
        positions[0][3].setPiece(Queen(false))
        positions[7][3].setPiece(Queen(true))
        positions[0][4].setPiece(blackKing)
        positions[7][4].setPiece(whiteKing)
        Array(2) { col ->
            positions[0][2 + col * 3].setPiece(Bishop(false))
            positions[7][2 + col * 3].setPiece(Bishop(true))
            positions[0][1 + col * 5].setPiece(Knight(false))
            positions[7][1 + col * 5].setPiece(Knight(true))
            positions[0][0 + col * 7].setPiece(Rook(false))
            positions[7][0 + col * 7].setPiece(Rook(true))
        }
        Array(positions.size) { col ->
            blackPieces.add(positions[0][col].getPiece()!!)
            piecesPositions.put(positions[0][col].getPiece()!!, getPosition(0, col))
            whitePieces.add(positions[7][col].getPiece()!!)
            piecesPositions.put(positions[7][col].getPiece()!!, getPosition(7, col))
        }
    }

    fun getPosition(y: Int, x: Int): Position {
        return positions[y][x]
    }

    fun getPiecePosition(piece: Piece): Position {
        return piecesPositions[piece]!!
    }

    fun getKing(isWhite: Boolean): Piece {
        return if (isWhite) whiteKing else blackKing
    }

    fun getBoardPositions(): Array<Array<Position>> {
        return positions
    }

    fun getPieces(isWhite: Boolean): LinkedList<Piece> {
        return if (isWhite) whitePieces else blackPieces
    }


    /**
     * Remove killed Pieces
     */
    fun removePiece(piece: Piece) {
        piecesPositions.remove(piece)
        if (piece.isWhite()) whitePieces.remove(piece)
        else blackPieces.remove(piece)
    }

    /**
     * Swap piece from a start to an end
     */
    fun changePosition(sourcePiece: Piece, start: Position?, end: Position?) {
        start?.setPiece(null)
        end?.setPiece(sourcePiece)
        piecesPositions[sourcePiece] = end!!
    }

    /**
     * will promote a selected Piece TODO
     */
    fun setPawnpromotion(piece: Piece, promotion: Piece, position: Position) {
        if (piece.isWhite()) {
            whitePieces.remove(piece)
            whitePieces.add(promotion)
        } else {
            blackPieces.add(piece)
            blackPieces.remove(promotion)
        }
        piecesPositions.put(promotion, position)
    }

    /**
     * Change rook position on castling Move (Especial Move)
     */
    fun changeRookOnCastling(move: CastlingMove) {
        val piece = move.getCastlePiece()
        val oldPosition: Position = piecesPositions[piece]!!
        val newPos: Position
        if (move.getIsQueenSide()) {
            newPos = positions[oldPosition.getY()][oldPosition.getX() + 3]
        } else {
            newPos = positions[oldPosition.getY()][oldPosition.getX() - 2]
        }
        changePosition(piece, oldPosition, newPos)
    }

    /**
     * Get a possible move from a star to an end
     * @return Move
     */
    fun getPossibleMove(player: Player, startX: Int, startY: Int, endX: Int, endY: Int): Move? {
        val startPositon = positions[startY][startX]
        val endPosition = positions[endY][endX]
        if (startPositon.getPiece() == null) return null
        var move: Move
        when {
            //Return Castling
            startPositon.getPiece() is King && abs(endX - endX) == 2 -> {
                val queenSide: Boolean = endX - endY < 0
                move = CastlingMove(queenSide, if (queenSide) "O-O-O" else "O-O", Types.K)
                if (move.getIsQueenSide()) {
                    move.setEnd(positions[endY][2])
                    move.setCastlePiece(positions[endY][0])
                } else {
                    move.setEnd(positions[endY][6])
                    move.setCastlePiece(positions[endY][7])
                }
                return move
            }
            //Return Attack
            endPosition.getPiece() != null -> {
                move = AttackMove(endPosition.getPng(), startPositon.getPiece()!!.getType())
            }
            //Return RegularMove
            else -> move = RegularMove(endPosition.getPng(), startPositon.getPiece()!!.getType())
        }
        move.setStart(startPositon)
        move.setEnd(endPosition)
        return move
    }

    /**
     * Complete a created move from a PNG
     * @param move uncompleted move
     */
    fun setDailyPositions(player: Player, move: Move) {
        when (move) {
            is CastlingMove -> {
                move.setStart(piecesPositions[getKing(player.isWhiteSide())]!!)
                val position = getPiecePosition(getKing(player.isWhiteSide()))
                if (move.getIsQueenSide()) {
                    move.setEnd(positions[position.getY()][2])
                    move.setCastlePiece(positions[position.getY()][0])
                } else {
                    move.setEnd(positions[position.getY()][6])
                    move.setCastlePiece(positions[position.getY()][7])
                }
            }
            is RegularMove -> {
                move.setEnd(pngMap[move.png.substring(move.png.length - 2, move.png.length)]!!)
                if (move.png.length > 3)
                    move.setDisambiguating(move.png[1])
            }
            is AttackMove -> {
                move.setEnd(pngMap[move.png.substring(move.png.length - 2, move.png.length)]!!)
                if (move.png.length > 4)
                    move.setDisambiguating(move.png[1])
                else if (move.png[0].isLowerCase())
                    move.setDisambiguating(move.png[0])
            }
        }
    }
}