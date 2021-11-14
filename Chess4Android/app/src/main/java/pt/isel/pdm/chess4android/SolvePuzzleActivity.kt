package pt.isel.pdm.chess4android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.Tile
import androidx.lifecycle.ViewModelProvider




class SolvePuzzleActivity : MainActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val solvePuzzleViewModel= ViewModelProvider(this)[SolvePuzzleViewModel::class.java]
        if(savedInstanceState== null){
            val bundle: Bundle? = intent.extras
            solvePuzzleViewModel.getBoard(bundle?.get("board") as Array<Array<Pair<Army, Piece>>>?)
        }else{

        }
        solvePuzzleViewModel.getBoard().observe(this){
            binding.boardView.dailyBoard=it
            binding.boardView.init()
        }
        solvePuzzleViewModel.getBoard()
        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            val randomArmy = Army.values()[1]
            val randomPiece = Piece.values()[1]
            tile.piece = Pair(randomArmy, randomPiece)
            solvePuzzleViewModel.saveBoard(binding.boardView.dailyBoard)
        }
    }
}