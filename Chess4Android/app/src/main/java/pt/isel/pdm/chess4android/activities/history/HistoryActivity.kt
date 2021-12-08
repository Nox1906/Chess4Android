package pt.isel.pdm.chess4android.activities.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import pt.isel.pdm.chess4android.R
import pt.isel.pdm.chess4android.databinding.ActivityHistoryBinding
import pt.isel.pdm.chess4android.model.game.DailyGame

class HistoryActivity : AppCompatActivity() {
    var d : DailyGame= DailyGame(puzzleId = "U2wRAGkr", puzzlePgn = "e4 c5 Nf3 Nc6 Bc4 e6 d3 g6 Bg5 Qb6 Qc1 Bg7 Nc3 Na5 O-O Nxc4 dxc4 Bxc3 bxc3 f6 Bh6 Nxh6 Qxh6 Qd6 Nh4 Qf8 Qe3 b6 e5 f5 Qf3 Rb8 Rfd1 Bb7 Qd3 Bc6 Nf3 Qg7 a4 O-O a5 Ra8 h3 Rfd8 axb6 axb6 Rxa8 Rxa8 Rb1 Ra2 Rxb6 Qh6 Rb8+ Kf7 Qd6 Ra1+ Kh2"
        ,puzzleSolution = arrayOf("h6f4","g2g3","a1h1","h2h1","f4f3","h1g1","f3g2"))

    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.puzzleList.layoutManager= LinearLayoutManager(this)
        d.setDailyGameStatus(true)
        binding.puzzleList.adapter = HistoryAdapter(
            listOf(d)
        ,this)

    }

}