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

private const val PUZZLE_EXTRA = "HistoryActivity.Extra.DailyPuzzle"
private const val ORIGINAL_PUZZLE_EXTRA = "HistoryActivity.Extra.OriginalDailyPuzzle"

class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    companion object {
        fun buildIntent(origin: Activity, puzzle: DailyPuzzle, originalDailyPuzzle: OriginalDailyPuzzle?): Intent {
            val msg: Intent =
                if (puzzle.getPuzzleSolution()[0] != "") {
                    Intent(origin, DailyPuzzleActivity::class.java)
                } else {
                    Intent(origin, HistoryPuzzleActivity::class.java)

                }
            val extras = Bundle()

            extras.putParcelable(ORIGINAL_PUZZLE_EXTRA,originalDailyPuzzle)
            extras.putParcelable(PUZZLE_EXTRA, puzzle)
            msg.putExtras(extras)
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
                startActivity(buildIntent(this, puzzle, historyViewModel.originalDailyPuzzle))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (historyViewModel.loadHistory()).observe(this) {
            binding.puzzleList.adapter = HistoryAdapter(
                it,
                historyViewModel.getApplication<DailyPuzzleChessApplication>().resources
            ) { puzzle ->
                startActivity(buildIntent(this, puzzle,historyViewModel.originalDailyPuzzle))
            }
        }
    }
}




