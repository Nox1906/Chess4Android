package pt.isel.pdm.chess4android

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding
import pt.isel.pdm.chess4android.views.Tile


open class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel.resultOfDailyPuzzleObserver.observe(this) {
            viewModel.resultOfDailyPuzzle = it
            viewModel.setDailyBoardMatrix()
            binding.boardView.dailyBoard=viewModel.getDailyBoardMatrix()
            binding.boardView.init()
        }
        viewModel.getDailyPuzzle()

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.credits) {
            startActivity(Intent(this, CreditsActivity::class.java))
            return true
        }
        if (item.itemId == R.id.dailyPuzzle) {
            startActivity(Intent(this, MainActivity::class.java))
            return true
        }
        if (item.itemId == R.id.solvePuzzle) {
            intent = Intent(this, SolvePuzzleActivity::class.java)
            intent.putExtra("board", viewModel.getDailyBoardMatrix())
            startActivity(intent)
            return true
        } else {

            super.onOptionsItemSelected(item)
        }
        return false
    }

}