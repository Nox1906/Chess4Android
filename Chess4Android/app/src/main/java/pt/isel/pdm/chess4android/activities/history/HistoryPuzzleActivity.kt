package pt.isel.pdm.chess4android.activities.history

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleActivity
import pt.isel.pdm.chess4android.databinding.ActivityHistoryPuzzleBinding
import pt.isel.pdm.chess4android.model.game.DailyPuzzle

private const val PUZZLE_EXTRA = "HistoryActivity.Extra.DailyPuzzle"

class HistoryPuzzleActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityHistoryPuzzleBinding.inflate(layoutInflater)
    }
    private val historyPuzzleViewModel: HistoryPuzzleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val puzzleDbObj = intent.getParcelableExtra<DailyPuzzleDbObject>(PUZZLE_EXTRA)
        var puzzle : DailyPuzzle? = null
        if (puzzleDbObj != null) {
            puzzle = DailyPuzzle(
                puzzleDbObj.puzzleId,
                puzzleDbObj.originalPuzzlePgn,
                puzzleDbObj.originalPuzzleSolution
            )
            binding.boardView.displayBoard(puzzle.board.getBoardPositions())
        }

        historyPuzzleViewModel.puzzleObserver.observe(this){
            val intent = Intent(this, DailyPuzzleActivity::class.java)
            intent.putExtra(PUZZLE_EXTRA, it)
            startActivity(intent)
        }

        binding.solvePuzzleButton.setOnClickListener {
            if (historyPuzzleViewModel.getPuzzle() == null){
                historyPuzzleViewModel.setPuzzle(puzzle)
                historyPuzzleViewModel.saveCurrentStateInDB()
            }else{
                historyPuzzleViewModel.getPuzzleFromDB()
            }
        }
        binding.seeSolutionButton.setOnClickListener {
            val intent = Intent(this, SolvedPuzzleActivity::class.java)
            val solvedPuzzle = DailyPuzzle(puzzleDbObj!!.puzzleId, puzzleDbObj.puzzlePgn, puzzleDbObj.puzzleSolution)
            intent.putExtra(PUZZLE_EXTRA, solvedPuzzle)
            startActivity(intent)
        }
    }
}
