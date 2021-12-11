package pt.isel.pdm.chess4android.activities.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pt.isel.pdm.chess4android.activities.about.AboutActivity
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleActivity
import pt.isel.pdm.chess4android.databinding.ActivitySolvedDailyPuzzleBinding
import pt.isel.pdm.chess4android.databinding.ActivityDailyPuzzleBinding
import pt.isel.pdm.chess4android.model.game.DailyPuzzle
private const val PUZZLE_EXTRA = "DailyPuzzleActivity.Extra.DailyGame"

class SolvedDailyPuzzleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySolvedDailyPuzzleBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val puzzle = intent.getParcelableExtra<DailyPuzzle>(PUZZLE_EXTRA)
        if (puzzle != null) {
            binding.boardView.displayBoard(puzzle.board.getBoardPositions())
        }
        binding.solvePuzzleButton.setOnClickListener {
            val intent =Intent(this, DailyPuzzleActivity::class.java)
            intent.putExtra("DailyPuzzleActivity.Extra.DailyGame",puzzle)
            startActivity(intent)
        }
        binding.seeSolutionButton.setOnClickListener {
            puzzle?.setPgn(puzzle.getPgn()+puzzle.getPuzzleSolution().joinToString(separator = " "))
            if (puzzle != null) {
                binding.boardView.displayBoard(puzzle.board.getBoardPositions())
            }
        }
    }
}