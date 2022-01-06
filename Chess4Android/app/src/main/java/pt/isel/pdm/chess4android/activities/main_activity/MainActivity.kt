package pt.isel.pdm.chess4android.activities.main_activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.activities.about.AboutActivity
import pt.isel.pdm.chess4android.activities.daily_puzzle.DailyPuzzleActivity
import pt.isel.pdm.chess4android.activities.history.HistoryActivity
import pt.isel.pdm.chess4android.activities.regular_game.RegularGameActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fetchNewGameButton.setOnClickListener {
            startActivity(Intent(this, RegularGameActivity::class.java))
        }
        binding.fetchPuzzleButton.setOnClickListener {
            startActivity(Intent(this, DailyPuzzleActivity::class.java))
        }
        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        binding.historyButton.setOnClickListener{
           startActivity(Intent(this,HistoryActivity::class.java ))
        }
    }
}