package pt.isel.pdm.chess4android.activities.history

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleActivity
import pt.isel.pdm.chess4android.common.DailyPuzzleChessApplication
import pt.isel.pdm.chess4android.databinding.ActivityHistoryBinding
import pt.isel.pdm.chess4android.model.game.DailyPuzzle

private const val PUZZLE_EXTRA = "DailyPuzzleActivity.Extra.DailyGame"

class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    companion object {
        fun buildIntent(origin: Activity, puzzle: DailyPuzzle): Intent {
            val msg: Intent = if (puzzle.getPuzzleSolution()[0] !="") {
                Intent(origin, DailyPuzzleActivity::class.java)
            } else {
                puzzle.originalPgn?.let { puzzle.setPgn(it) }
                puzzle.originalSolution?.let { puzzle.setSolution(it) }
                Intent(origin, SolvedDailyPuzzleActivity::class.java)
            }
            msg.putExtra(PUZZLE_EXTRA, puzzle)
            return msg
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.puzzleList.layoutManager = LinearLayoutManager(this)
        (historyViewModel.puzzleHistory ?: historyViewModel.loadHistory()).observe(this) {
            binding.puzzleList.adapter = HistoryAdapter(
                it,
                historyViewModel.getApplication<DailyPuzzleChessApplication>().resources
            ) { puzzle ->
                startActivity(buildIntent(this, puzzle))
            }
        }
    }


}

