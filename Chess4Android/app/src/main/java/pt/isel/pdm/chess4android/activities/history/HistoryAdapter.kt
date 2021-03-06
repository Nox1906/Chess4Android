package pt.isel.pdm.chess4android.activities.history

import android.animation.ValueAnimator
import android.content.res.Resources
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.chess4android.R

class HistoryItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val puzzleId: TextView = itemView.findViewById(R.id.puzzleId)
    private val isSolved: TextView = itemView.findViewById(R.id.solved)
    private val puzzleDate: TextView = itemView.findViewById(R.id.puzzle_date)

    fun bindTo(puzzleDbObj: DailyPuzzleDbObject, resources: Resources, onItemCLick: () -> Unit) {
        puzzleId.text = puzzleDbObj.puzzleId
        isSolved.text =
            if (puzzleDbObj.puzzleSolution[0] == "") resources.getString(R.string.textView_isSolved) else resources.getString(
                R.string.textView_notSolved
            )
        itemView.setOnClickListener {
            itemView.isClickable = false
            startAnimation {
                onItemCLick()
                itemView.isClickable = true
            }
        }
        puzzleDate.text= puzzleDbObj.puzzleDate
    }

    private fun startAnimation(onAnimationEnd: () -> Unit) {

        val animation = ValueAnimator.ofArgb(
            ContextCompat.getColor(itemView.context, R.color.list_item_background),
            ContextCompat.getColor(itemView.context, R.color.list_item_background_selected),
            ContextCompat.getColor(itemView.context, R.color.list_item_background)
        )

        animation.addUpdateListener { animator ->
            val background = itemView.background as GradientDrawable
            background.setColor(animator.animatedValue as Int)
        }

        animation.duration = 400
        animation.doOnEnd { onAnimationEnd() }

        animation.start()
    }
}

class HistoryAdapter(
    private val dataSource: List<DailyPuzzleDbObject>,
    private val resources: Resources,
    private val onItemCLick: (DailyPuzzleDbObject) -> Unit
) :
    RecyclerView.Adapter<HistoryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_history_item, parent, false)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bindTo(dataSource[position], resources) {
            onItemCLick(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}
