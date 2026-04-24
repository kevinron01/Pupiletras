package com.kevinron.pupiletras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WinActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LEVEL_ID = "level_id"
        const val EXTRA_ELAPSED_MS = "elapsed_ms"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        val levelId = intent.getIntExtra(EXTRA_LEVEL_ID, 1)
        val elapsedMs = intent.getLongExtra(EXTRA_ELAPSED_MS, 0L)
        val seconds = elapsedMs / 1000
        val timeString = String.format("%02d:%02d", seconds / 60, seconds % 60)

        findViewById<TextView>(R.id.tvWinTitle).text = "¡Felicidades!"
        findViewById<TextView>(R.id.tvWinSub).text =
            "Completaste el Nivel $levelId en $timeString"

        val nextId = GameData.nextLevelId(levelId)
        val nextBtn = findViewById<Button>(R.id.btnNextLevel)
        if (nextId != null) {
            nextBtn.visibility = android.view.View.VISIBLE
            nextBtn.setOnClickListener {
                startActivity(Intent(this, GameActivity::class.java).apply {
                    putExtra(GameActivity.EXTRA_LEVEL_ID, nextId)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            }
        }

        findViewById<Button>(R.id.btnReplay).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_LEVEL_ID, levelId)
            })
            finish()
        }

        findViewById<Button>(R.id.btnMenu).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }
    }
}
