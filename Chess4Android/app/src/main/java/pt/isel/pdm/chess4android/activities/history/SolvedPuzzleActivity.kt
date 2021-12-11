package pt.isel.pdm.chess4android.activities.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isel.pdm.chess4android.databinding.ActivitySolvedPuzzleBinding
import pt.isel.pdm.chess4android.model.game.DailyPuzzle

private const val PUZZLE_EXTRA = "HistoryActivity.Extra.DailyPuzzle"
class SolvedPuzzleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySolvedPuzzleBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val puzzle = intent.getParcelableExtra<DailyPuzzle>(PUZZLE_EXTRA)

        if (puzzle != null) {
            binding.boardView.displayBoard(puzzle.board.getBoardPositions())
        }
    }
}