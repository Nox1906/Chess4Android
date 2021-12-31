package pt.isel.pdm.chess4android.activities.daily_puzzle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleViewModel.*
import pt.isel.pdm.chess4android.databinding.ActivityDailyPuzzleBinding
import pt.isel.pdm.chess4android.views.Tile
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.model.game.DailyPuzzle


private const val PUZZLE_EXTRA = "HistoryActivity.Extra.DailyPuzzle"

class DailyPuzzleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDailyPuzzleBinding.inflate(layoutInflater)
    }

    private val viewModel: DailyPuzzleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val puzzle = intent.extras?.getParcelable<DailyPuzzle>(PUZZLE_EXTRA)
        viewModel.dailyPuzzle.observe(this) {
            binding.boardView.displayBoard(it.board.getBoardPositions())
            displayCurrentPlayer()
        }
        if (puzzle == null) {
            viewModel.getDailyPuzzle()
        } else if (viewModel.dailyPuzzle.value == null) {
            viewModel.setDailyGame(puzzle)
        }

        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            if (tile.piece?.isWhite() != viewModel.isWhitePlayer() && !viewModel.onMove && !viewModel.solutionIsDone()!!) {
                Toast.makeText(this, R.string.wrong_turn, Toast.LENGTH_LONG).show()
            } else {
                if (!viewModel.solutionIsDone()!!) {
                    if (tile.piece != null && !viewModel.onMove) {
                        tile.isSel = true
                        tile.invalidate()
                        viewModel.previousTile = PreviousTile(tile, row, column)
                        viewModel.onMove = true
                    } else if (viewModel.previousTile != null && viewModel.onMove && viewModel.canMove(
                            column,
                            row
                        ) == true
                    ) {
                        tile.piece = viewModel.previousTile?.tile?.piece
                        viewModel.onMove = false
                        tile.isSel = false
                        viewModel.previousTile?.tile?.piece = null
                        viewModel.previousTile?.tile?.isSel = false
                        viewModel.previousTile = null
                        viewModel.removeSolutionMove()
                        displayCurrentPlayer()
                        viewModel.saveCurrentStateInDB()

                    } else if (viewModel.onMove && tile.piece != null) {
                        viewModel.onMove = false
                        viewModel.previousTile?.tile?.isSel = false
                        viewModel.previousTile?.tile?.invalidate()

                    } else if (tile == viewModel.previousTile?.tile && viewModel.onMove) {
                        tile.invalidate()
                        viewModel.onMove = false
                        tile.isSel = false
                        viewModel.previousTile = null

                    } else if (tile.piece == null && viewModel.onMove) {
                        tile.isSel = false
                        viewModel.onMove = false
                        viewModel.previousTile?.tile?.isSel = false
                        viewModel.previousTile?.tile?.invalidate()
                        viewModel.previousTile = null
                    } else {
                        viewModel.onMove = false
                        tile.isSel = false
                    }
                } else {

                    Toast.makeText(this, R.string.solution_achieved, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayCurrentPlayer() {
        if (!viewModel.solutionIsDone()!!) {
            if (viewModel.isWhitePlayer())
                binding.currentPlayerView.text = getString(R.string.current_player_white)
            else
                binding.currentPlayerView.text = getString(R.string.current_player_black)
        } else {
            binding.currentPlayerView.text = getString(R.string.solution_achieved)
        }
    }

}