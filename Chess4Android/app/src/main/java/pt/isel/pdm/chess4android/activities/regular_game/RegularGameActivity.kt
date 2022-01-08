package pt.isel.pdm.chess4android.activities.regular_game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.databinding.ActivityRegularGameBinding
import pt.isel.pdm.chess4android.model.game.RegularGame
import pt.isel.pdm.chess4android.views.Tile


class RegularGameActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegularGameBinding.inflate(layoutInflater)
    }

    private val viewModel: RegularGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.regularGameObserver.observe(this) {
            binding.boardView.displayBoard(it.board.getBoardPositions())
            displayCurrentPlayer()
        }

        if (viewModel.regularGameObserver.value == null)
            viewModel.maybeGetRegularGameFromDB()

        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            if (tile.piece?.isWhite() != viewModel.isWhitePlayer() && !viewModel.onMove) {
                Toast.makeText(this, R.string.wrong_turn, Toast.LENGTH_LONG).show()
            } else {
                if (tile.piece != null && !viewModel.onMove) {
                    tile.isSel = true
                    tile.invalidate()
                    viewModel.previousTile =
                        RegularGameViewModel.PreviousTile(tile, row, column)
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
                    viewModel.saveCurrentStateInDB()
                    displayCurrentPlayer()

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
            }
        }
    }

    private fun displayCurrentPlayer() {
        if (viewModel.isWhitePlayer())
            binding.currentPlayerView.text = getString(R.string.current_player_white)
        else
            binding.currentPlayerView.text = getString(R.string.current_player_black)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.regular_game_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
             R.id.reset-> {

                 viewModel.regularGameObserver.value?.resetBoard()
                 binding.boardView.invalidate()
                 binding.boardView.displayBoard(viewModel.regularGameObserver.value?.board?.getBoardPositions())

                 viewModel.saveCurrentStateInDB()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}