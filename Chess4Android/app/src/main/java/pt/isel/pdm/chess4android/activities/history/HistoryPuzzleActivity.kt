package pt.isel.pdm.chess4android.activities.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleActivity
import pt.isel.pdm.chess4android.databinding.ActivityHistoryPuzzleBinding
import pt.isel.pdm.chess4android.model.game.DailyPuzzle

private const val PUZZLE_EXTRA = "HistoryActivity.Extra.DailyPuzzle"
private const val ORIGINAL_PUZZLE_EXTRA = "HistoryActivity.Extra.OriginalDailyPuzzle"

class HistoryPuzzleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHistoryPuzzleBinding.inflate(layoutInflater)
    }
    private val solvedDailyPuzzleViewModel: HistoryPuzzleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val puzzle = intent.extras?.getParcelable<DailyPuzzle>(PUZZLE_EXTRA)
        val originalPuzzle = intent.extras?.getParcelable<OriginalDailyPuzzle>(ORIGINAL_PUZZLE_EXTRA)
        var newPuzzle : DailyPuzzle? = null
        if (puzzle != null && originalPuzzle != null) {

            newPuzzle = DailyPuzzle(
                puzzle.getPuzzleId(),
                originalPuzzle.originalPuzzlePgn,
                originalPuzzle.originalPuzzleSolution
            )
            binding.boardView.displayBoard(newPuzzle.board.getBoardPositions())
        }

        solvedDailyPuzzleViewModel.puzzle.observe(this){
            val intent = Intent(this, DailyPuzzleActivity::class.java)
            intent.putExtra("HistoryActivity.Extra.DailyPuzzle", solvedDailyPuzzleViewModel.getPuzzle())
            startActivity(intent)
        }

        binding.solvePuzzleButton.setOnClickListener {
            if (solvedDailyPuzzleViewModel.getPuzzle() == null){
                solvedDailyPuzzleViewModel.setPuzzle(newPuzzle)
                solvedDailyPuzzleViewModel.saveCurrentStateInDB()
            }else{
                solvedDailyPuzzleViewModel.getPuzzleFromDB(
                    solvedDailyPuzzleViewModel.getPuzzle()?.getPuzzleId()
                )
            }
        }
        binding.seeSolutionButton.setOnClickListener {
            val intent = Intent(this, SolvedPuzzleActivity::class.java)
            intent.putExtra("HistoryActivity.Extra.DailyPuzzle", puzzle)
            startActivity(intent)
        }
    }
}
