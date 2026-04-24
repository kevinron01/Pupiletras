package com.kevinron.pupiletras

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WinActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLAYER_NAME = "player_name"
        const val EXTRA_DIFFICULTY = "difficulty"
        const val EXTRA_LEVEL_NUMBER = "level_number"
        const val EXTRA_ELAPSED_MS = "elapsed_ms"
        const val EXTRA_SCORE = "score"
        const val EXTRA_TOTAL_SCORE = "total_score"
        const val EXTRA_COMPLETION_BONUS = "completion_bonus"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        val playerName = intent.getStringExtra(EXTRA_PLAYER_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: MainActivity.playerName(this).ifBlank { "Jugador" }
        val difficulty = Difficulty.fromKey(intent.getStringExtra(EXTRA_DIFFICULTY))
        val levelNumber = intent.getIntExtra(EXTRA_LEVEL_NUMBER, 1)
        val elapsedMs = intent.getLongExtra(EXTRA_ELAPSED_MS, 0L)
        val levelScore = intent.getIntExtra(EXTRA_SCORE, 0)
        val totalScore = intent.getIntExtra(EXTRA_TOTAL_SCORE, GameProgressStore.totalScore(this))
        val bonus = intent.getIntExtra(EXTRA_COMPLETION_BONUS, 0)
        val seconds = elapsedMs / 1000
        val timeString = String.format("%02d:%02d", seconds / 60, seconds % 60)
        val isLastLevel = levelNumber >= GameData.MAX_LEVELS_PER_DIFFICULTY

        findViewById<TextView>(R.id.tvWinTitle).text = "¡Bravo, $playerName!"
        findViewById<TextView>(R.id.tvWinSub).text =
            "Terminaste el nivel $levelNumber en ${difficulty.title.lowercase()} en $timeString."
        findViewById<TextView>(R.id.tvWinStats).text =
            if (isLastLevel) {
                "Completaste los ${GameData.MAX_LEVELS_PER_DIFFICULTY} niveles de ${difficulty.title.lowercase()}. Ganaste $levelScore puntos en esta partida. Total acumulado: $totalScore."
            } else {
                "Ganaste $levelScore puntos en esta partida. Bono final: +$bonus. Total acumulado: $totalScore."
            }

        val nextButton = findViewById<Button>(R.id.btnNextLevel)
        if (isLastLevel) {
            nextButton.visibility = View.GONE
        } else {
            nextButton.visibility = View.VISIBLE
            nextButton.setOnClickListener {
                startActivity(Intent(this, GameActivity::class.java).apply {
                    putExtra(GameActivity.EXTRA_PLAYER_NAME, playerName)
                    putExtra(GameActivity.EXTRA_DIFFICULTY, difficulty.key)
                    putExtra(GameActivity.EXTRA_LEVEL_NUMBER, levelNumber + 1)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                finish()
            }
        }

        findViewById<Button>(R.id.btnReplay).text = "Jugar otra vez"
        findViewById<Button>(R.id.btnReplay).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_PLAYER_NAME, playerName)
                putExtra(GameActivity.EXTRA_DIFFICULTY, difficulty.key)
                putExtra(GameActivity.EXTRA_LEVEL_NUMBER, levelNumber)
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
