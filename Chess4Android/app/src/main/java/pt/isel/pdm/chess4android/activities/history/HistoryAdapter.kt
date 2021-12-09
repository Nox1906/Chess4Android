package pt.isel.pdm.chess4android.activities.history

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.common.DailyGameChessApplication
import pt.isel.pdm.chess4android.model.game.DailyGame

class HistoryItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    private val puzzleId: TextView = itemView.findViewById(R.id.puzzleId)
    private val isSolved: TextView = itemView.findViewById(R.id.solved)

    fun bindTo(dailyGame: DailyGame, resources: Resources) {
        puzzleId.text = dailyGame.getPuzzleId()
        isSolved.text = if(dailyGame.getDailyGameStatus()) resources.getString(R.string.textView_isSolved)  else resources.getString(R.string.textView_notSolved)
    }


}
class HistoryAdapter(private val dataSource: List<DailyGame>, private val resources: Resources) :
    RecyclerView.Adapter<HistoryItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_history_item, parent, false)
        return HistoryItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bindTo(dataSource[position], resources)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

}