package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.GridLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.MainActivityViewModel.*
import pt.isel.pdm.chess4android.PuzzleInfo
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.views.Tile.Type
import java.lang.Math.abs
import java.util.*


typealias TileTouchListener = (tile: Tile, row: Int, column: Int) -> Unit

/**
 * Custom view that implements a chess board.
 */
@SuppressLint("ClickableViewAccessibility")
class BoardView(private val ctx: Context, attrs: AttributeSet?) : GridLayout(ctx, attrs) {

    private val side = 8

    private val brush = Paint().apply {
        ctx.resources.getColor(R.color.chess_board_black, null)
        style = Paint.Style.STROKE
        strokeWidth = 10F
    }

    private fun createImageEntry(army: Army, piece: Piece, imageId: Int) =
        Pair(Pair(army, piece), VectorDrawableCompat.create(ctx.resources, imageId, null))

    private val piecesImages = mapOf(
        createImageEntry(Army.WHITE, Piece.PAWN, R.drawable.ic_white_pawn),
        createImageEntry(Army.WHITE, Piece.KNIGHT, R.drawable.ic_white_knight),
        createImageEntry(Army.WHITE, Piece.BISHOP, R.drawable.ic_white_bishop),
        createImageEntry(Army.WHITE, Piece.ROOK, R.drawable.ic_white_rook),
        createImageEntry(Army.WHITE, Piece.QUEEN, R.drawable.ic_white_queen),
        createImageEntry(Army.WHITE, Piece.KING, R.drawable.ic_white_king),
        createImageEntry(Army.BLACK, Piece.PAWN, R.drawable.ic_black_pawn),
        createImageEntry(Army.BLACK, Piece.KNIGHT, R.drawable.ic_black_knight),
        createImageEntry(Army.BLACK, Piece.BISHOP, R.drawable.ic_black_bishop),
        createImageEntry(Army.BLACK, Piece.ROOK, R.drawable.ic_black_rook),
        createImageEntry(Army.BLACK, Piece.QUEEN, R.drawable.ic_black_queen),
        createImageEntry(Army.BLACK, Piece.KING, R.drawable.ic_black_king),
    )

    var puzzleInfo: PuzzleInfo? = null

    private var dailyBoard = Array(side) {
        Array(side) { Pair(Army.EMPTY, Piece.EMPTY) }
    }

    fun init() {
        Log.v("TAG PuzzleInfo", puzzleInfo?.game?.pgn.toString())
        generateBoardMatrix(puzzleInfo, dailyBoard)
        Log.v("Daily board ", dailyBoard.contentDeepToString())
        rowCount = side
        columnCount = side
        repeat(side * side) {
            val row = it / side
            val column = it % side
            val tile = Tile(
                ctx,
                if ((row + column) % 2 == 0) Type.WHITE else Type.BLACK,
                side,
                piecesImages,
                if (dailyBoard[abs(row-(side-1))][column] != Pair(
                        Army.EMPTY,
                        Piece.EMPTY
                    )
                ) dailyBoard[abs(row-(side-1))][column] else null
            )
            tile.setOnClickListener { onTileClickedListener?.invoke(tile, row, column) }
            addView(tile)
        }
    }

    var onTileClickedListener: TileTouchListener? = null

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.drawLine(0f, 0f, width.toFloat(), 0f, brush)
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), brush)
        canvas.drawLine(0f, 0f, 0f, height.toFloat(), brush)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), brush)
    }

    private fun generateBoardMatrix(
        puzzleInfo: PuzzleInfo?,
        boardMatrix: Array<Array<Pair<Army, Piece>>>
    ) {
        puzzleInfo?.game?.pgn?.split("\\s".toRegex())?.forEach { item ->
            if(item=="O-O") return //so para testar posicionamento correto das peças
            val piecePosition = getBoardPosition(item)
            boardMatrix[piecePosition.row][piecePosition.col] = piecePosition.pair
        }
    }

    data class PiecePosition(val row: Int, val col: Int, val pair: Pair<Army, Piece>)

    private fun getBoardPosition(pgn: String): PiecePosition {
        if (pgn.length == 2 && !pgn.contains('+')) {
            return PiecePosition(
                (pgn[1].code - '0'.code) - 1, Columns.valueOf(
                    pgn[0].toString()
                        .uppercase(Locale.getDefault())
                ).ordinal, Pair(Army.WHITE, Piece.PAWN)
            )
        } else if (pgn.length == 3 && !pgn.contains('+')) {
            return PiecePosition(
                (pgn[2].code - '0'.code) - 1, Columns.valueOf(
                    pgn[1].toString()
                        .uppercase(Locale.getDefault())
                ).ordinal, Pair(Army.WHITE, getPiece(pgn[0]))
            )
        }
        //else if(pgn==4)
        return PiecePosition(0, 0, Pair(Army.BLACK, Piece.PAWN))
    }

    private fun getPiece(letter: Char): Piece {
        if (letter == 'B') return Piece.BISHOP
        if (letter == 'Q') return Piece.QUEEN
        if (letter == 'K') return Piece.KING
        if (letter == 'N') return Piece.KNIGHT
        return if (letter == 'R') Piece.ROOK
        else Piece.KING
    }
}