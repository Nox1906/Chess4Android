package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.GridLayout
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.model.board.Position
import pt.isel.pdm.chess4android.model.pieces.*
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

    private fun createImageEntry(pieceType: Types,isWhite: Boolean, imageId: Int) =
        Pair(Pair(pieceType,isWhite), VectorDrawableCompat.create(ctx.resources, imageId, null))

    private val piecesImages = mapOf(
        createImageEntry(Types.P,true, R.drawable.ic_white_pawn),
        createImageEntry(Types.N,true ,R.drawable.ic_white_knight),
        createImageEntry(Types.B,true, R.drawable.ic_white_bishop),
        createImageEntry(Types.R,true, R.drawable.ic_white_rook),
        createImageEntry(Types.Q,true, R.drawable.ic_white_queen),
        createImageEntry(Types.K,true, R.drawable.ic_white_king),
        createImageEntry(Types.P,false, R.drawable.ic_black_pawn),
        createImageEntry(Types.N,false, R.drawable.ic_black_knight),
        createImageEntry(Types.B,false, R.drawable.ic_black_bishop),
        createImageEntry(Types.R,false, R.drawable.ic_black_rook),
        createImageEntry(Types.Q,false, R.drawable.ic_black_queen),
        createImageEntry(Types.K,false, R.drawable.ic_black_king),
    )



    fun displayBoard(dailyBoard:Array<Array<Position>>?) {

        Log.v("TAG PuzzleInfo", piecesImages.keys.toString())
        Log.v("Daily board ", dailyBoard.contentDeepToString())
        rowCount = side
        columnCount = side
        repeat(side * side) {
            val row = it / side
            val column = it % side
            val tile = Tile(
                ctx,
                side,
                piecesImages,
                if((row + column) % 2 == 0) Tile.Type.WHITE else Tile.Type.BLACK,
                false,
                if (dailyBoard?.get(row)?.get(column)?.getPiece() !=null)
                {
                    dailyBoard[row][column].getPiece()
                }
                else null
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


}