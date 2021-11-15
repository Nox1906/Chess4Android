package pt.isel.pdm.chess4android

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.model.game.DailyGame
import pt.isel.pdm.chess4android.views.Tile
import pt.isel.pdm.chess4android.SolvePuzzleViewModel.*


class SolvePuzzleActivity : MainActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val solvePuzzleViewModel: SolvePuzzleViewModel by viewModels()
    private var countTileTouch: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //val solvePuzzleViewModel = ViewModelProvider(this)[SolvePuzzleViewModel::class.java]
        if (solvePuzzleViewModel.getDailyGame() == null) {
            val bundle: Bundle? = intent.extras
            solvePuzzleViewModel.setDailyGame(bundle?.get("dailyGame") as DailyGame)
            solvePuzzleViewModel.setDailyGameState()
        }
        solvePuzzleViewModel.getDailyGameState()
            ?.let { binding.boardView.displayBoard(it.board.getBoardPositions()) }

        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->

            if (tile.piece != null && countTileTouch == 0) {
                tile.isSel = true
                solvePuzzleViewModel.previousTile = PreviousTile(tile, row, column)
                countTileTouch++
            } else if (solvePuzzleViewModel.previousTile != null && countTileTouch == 1 &&
                solvePuzzleViewModel.getDailyGame()?.playerMove(
                    solvePuzzleViewModel.getDailyGame()!!.currentPlayer,
                    solvePuzzleViewModel.previousTile!!.x,
                    solvePuzzleViewModel.previousTile!!.y,
                    column,
                    row
                ) == true
            ) {
                tile.piece = solvePuzzleViewModel.previousTile?.tile?.piece

                countTileTouch = 0
                solvePuzzleViewModel.previousTile?.tile?.piece = null
                solvePuzzleViewModel.previousTile?.tile?.isSel = false
                solvePuzzleViewModel.previousTile = null
                solvePuzzleViewModel.setDailyGameState()

            } else {
                countTileTouch = 0
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_solve_puzzle_activity, menu)
        return true

    }
}
// == true