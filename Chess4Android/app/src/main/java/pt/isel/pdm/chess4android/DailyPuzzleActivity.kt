package pt.isel.pdm.chess4android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.databinding.ActivityDailyPuzzleBinding
import pt.isel.pdm.chess4android.views.Tile

class DailyPuzzleActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDailyPuzzleBinding.inflate(layoutInflater)
    }

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.dailyGame.observe(this) {
            binding.boardView.displayBoard(it.board.getBoardPositions())
        }
        if (viewModel.dailyGameNotFetched()) {
            viewModel.getDailyPuzzle()
        }

        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            if (!viewModel.solutionIsDone()) {
                if (tile.piece != null && !viewModel.onMove) {
                    tile.isSel = true
                    tile.invalidate()
                    viewModel.previousTile = MainActivityViewModel.PreviousTile(tile, row, column)
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
                    viewModel.countMoves++

                } else if (viewModel.onMove && tile.piece != null) {
                    viewModel.onMove = false
                    viewModel.previousTile?.tile?.isSel = false
                    viewModel.previousTile?.tile?.invalidate()

                } else if (tile == viewModel.previousTile?.tile && viewModel.onMove) {
                    tile.invalidate()
                    viewModel.onMove = false
                    tile.isSel = false
                    viewModel.previousTile = null
                } else {
                    viewModel.onMove = false
                    tile.isSel = false
                }
            } else {
                Toast.makeText(this, "I've achieved the solution", Toast.LENGTH_LONG).show()
            }
        }
    }
}