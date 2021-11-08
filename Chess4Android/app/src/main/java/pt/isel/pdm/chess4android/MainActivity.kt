package pt.isel.pdm.chess4android

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.BoardView
import pt.isel.pdm.chess4android.views.Tile


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel:MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDailyBoard()

        viewModel.puzzleInfo.observe(this){
            binding.boardView.puzzleInfo=it
            binding.boardView.init()
        }
        setContentView(binding.root)
        binding.boardView.onTileClickedListener = { tile: Tile, row: Int, column: Int ->
            //val randomArmy = Army.values()[Random.nextInt(Army.values().indices)]
            //val randomPiece = Piece.values()[Random.nextInt(Piece.values().indices)]
            //tile.piece = Pair(randomArmy, randomPiece)
        }
    }
}