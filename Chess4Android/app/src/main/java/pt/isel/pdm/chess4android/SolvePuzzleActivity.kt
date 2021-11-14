package pt.isel.pdm.chess4android

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.Tile


class SolvePuzzleActivity : MainActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val solvePuzzleViewModel: SolvePuzzleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //val solvePuzzleViewModel = ViewModelProvider(this)[SolvePuzzleViewModel::class.java]
        if (solvePuzzleViewModel.board == null) {
            val bundle: Bundle? = intent.extras
            solvePuzzleViewModel.board =
                bundle?.get("board") as Array<Array<Pair<Army, Piece>>>?
            solvePuzzleViewModel.setBoardState()
        }
        solvePuzzleViewModel.getBoardState()?.let { binding.boardView.displayBoard(it) }

        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            val randomArmy = Army.values()[1]
            val randomPiece = Piece.values()[1]
            tile.piece = Pair(randomArmy, randomPiece)
            val pieceAndItsPosition =
                PieceAndItsPosition(row, column, Pair(randomArmy, randomPiece))
            solvePuzzleViewModel.updateBoard(pieceAndItsPosition)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_solve_puzzle_activity, menu)
        return true

    }
}