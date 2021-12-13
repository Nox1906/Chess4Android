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

class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModels()
    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    companion object {
        fun buildIntent(origin: Activity, puzzleDbObj: DailyPuzzleDbObject): Intent {
            val msg: Intent?
            if (puzzleDbObj.puzzleSolution[0] != "") { //not solved
                val dp = DailyPuzzle(puzzleDbObj.puzzleId, puzzleDbObj.puzzlePgn, puzzleDbObj.puzzleSolution)
                msg = Intent(origin, DailyPuzzleActivity::class.java)
                msg.putExtra(PUZZLE_EXTRA, dp)

            } else { //solved
                msg = Intent(origin, HistoryPuzzleActivity::class.java)
                msg.putExtra(PUZZLE_EXTRA, puzzleDbObj)
            }
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
            ) { puzzleDbObj ->
                startActivity(buildIntent(this, puzzleDbObj))
            }
        }
    }

    override fun onResume() { //update history adapter when back pressed
        super.onResume()
        (historyViewModel.loadHistory()).observe(this) {
            binding.puzzleList.adapter = HistoryAdapter(
                it,
                historyViewModel.getApplication<DailyPuzzleChessApplication>().resources
            ) { puzzleDbObj ->
                startActivity(buildIntent(this, puzzleDbObj))
            }
        }
    }
}




