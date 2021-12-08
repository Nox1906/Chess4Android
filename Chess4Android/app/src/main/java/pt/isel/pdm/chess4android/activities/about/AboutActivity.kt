package pt.isel.pdm.chess4android.activities.about

import android.os.Bundle
import pt.isel.pdm.chess4android.activities.main_activity.MainActivity
import pt.isel.pdm.chess4android.databinding.ActivityAboutBinding

class AboutActivity : MainActivity() {
    private val binding by lazy {
        ActivityAboutBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}