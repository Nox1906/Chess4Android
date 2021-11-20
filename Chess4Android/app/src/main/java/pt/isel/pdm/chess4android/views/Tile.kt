package pt.isel.pdm.chess4android.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import pt.isel.pdm.chess4android.Game
import pt.isel.pdm.chess4android.Puzzle
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.model.pieces.Piece
import pt.isel.pdm.chess4android.model.pieces.Types

/**
 * Custom view that implements a chess board tile.
 * Tiles are either black or white and can they can be empty or occupied by a chess piece.
 *
 * Implementation note: This view is not to be used with the designer tool.
 * You need to adapt this view to suit your needs. ;)
 *
 * @property type           The tile's type (i.e. black or white)
 * @property tilesPerSide   The number of tiles in each side of the chess board
 *
 */
@SuppressLint("ViewConstructor")
class Tile(
    private val ctx: Context,
    private val tilesPerSide: Int,
    private val images: Map<Pair<Types, Boolean>, VectorDrawableCompat?>,
    private val type: Type,
    var isSel: Boolean,
    initialPiece: Piece? = null,
) : View(ctx) {
    enum class Type { WHITE, BLACK }

    var piece: Piece? = initialPiece
        set(value) {
            field = value
            invalidate()
        }
    private val brush = Paint().apply {
        color = ctx.resources.getColor(
            if (type == Type.WHITE) R.color.chess_board_white else R.color.chess_board_black,
            null
        )
        style = Paint.Style.FILL_AND_STROKE
    }
    private val otherBrush = Paint().apply {
        color = Color.CYAN
    }
    private val circle= VectorDrawableCompat.create(ctx.resources, R.drawable.circle, null)
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val side = Integer.min(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
        setMeasuredDimension(side / tilesPerSide, side / tilesPerSide)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), brush)
        if (!isSel) {

            if (piece != null) {
                images[Pair(piece!!.getType(), piece!!.isWhite())]?.apply {
                    val padding = 8
                    setBounds(padding, padding, width - padding, height - padding)
                    draw(canvas)
                }

            }
        } else {
            circle?.apply {
                val padding=1
                setBounds(padding, padding, width - padding, height - padding)
                draw(canvas)
            }
            images[Pair(piece!!.getType(), piece!!.isWhite())]?.apply {
                val padding = 8
                setBounds(padding, padding, width - padding, height - padding)
                draw(canvas)
            }

        }
    }
}


