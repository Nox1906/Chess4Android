package pt.isel.pdm.chess4android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.isel.pdm.chess4android.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.fetchPuzzleButton.setOnClickListener {
            startActivity(Intent(this, DailyPuzzleActivity::class.java))
        }
        binding.aboutButton.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }
}