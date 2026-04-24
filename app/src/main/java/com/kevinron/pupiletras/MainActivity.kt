package com.kevinron.pupiletras

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_NAME = "pupiletras_prefs"
        private const val KEY_PLAYER_NAME = "player_name"

        fun currentLevel(context: Context, difficulty: Difficulty): Int {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getInt("level_${difficulty.key}", 1)
                .coerceIn(1, GameData.MAX_LEVELS_PER_DIFFICULTY)
        }

        fun unlockNextLevel(context: Context, difficulty: Difficulty, completedLevel: Int) {
            val nextLevel = (completedLevel + 1).coerceAtMost(GameData.MAX_LEVELS_PER_DIFFICULTY)
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            if (nextLevel > prefs.getInt("level_${difficulty.key}", 1)) {
                prefs.edit().putInt("level_${difficulty.key}", nextLevel).apply()
            }
        }

        fun playerName(context: Context): String {
            return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_PLAYER_NAME, "")?.trim().orEmpty()
        }

        fun savePlayerName(context: Context, name: String) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_PLAYER_NAME, name.trim())
                .apply()
        }
    }

    private lateinit var playerNameInput: EditText
    private lateinit var totalScoreView: TextView
    private lateinit var resumeCard: View
    private lateinit var resumeTitle: TextView
    private lateinit var resumeDesc: TextView
    private lateinit var resumeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerNameInput = findViewById(R.id.etPlayerName)
        totalScoreView = findViewById(R.id.tvTotalScore)
        resumeCard = findViewById(R.id.resumeCard)
        resumeTitle = findViewById(R.id.tvResumeTitle)
        resumeDesc = findViewById(R.id.tvResumeDesc)
        resumeButton = findViewById(R.id.btnResume)
    }

    override fun onResume() {
        super.onResume()
        playerNameInput.setText(playerName(this))
        totalScoreView.text = "Puntos totales: ${GameProgressStore.totalScore(this)}"
        renderResumeCard()
        bindDifficulty(
            buttonId = R.id.btnEasy,
            descId = R.id.tvEasyDesc,
            difficulty = Difficulty.EASY
        )
        bindDifficulty(
            buttonId = R.id.btnMedium,
            descId = R.id.tvMediumDesc,
            difficulty = Difficulty.MEDIUM
        )
        bindDifficulty(
            buttonId = R.id.btnHard,
            descId = R.id.tvHardDesc,
            difficulty = Difficulty.HARD
        )
    }

    private fun renderResumeCard() {
        val saved = GameProgressStore.loadActiveSession(this)
        if (saved == null) {
            resumeCard.visibility = View.GONE
            return
        }
        resumeCard.visibility = View.VISIBLE
        resumeTitle.text = "Partida guardada de ${saved.playerName}"
        resumeDesc.text =
            "${saved.difficulty.title} · Nivel ${saved.levelNumber}/${GameData.MAX_LEVELS_PER_DIFFICULTY} · ${saved.foundWords.size} palabras encontradas · ${saved.score} puntos"
        resumeButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_RESUME_SAVED_GAME, true)
            })
        }
    }

    private fun bindDifficulty(buttonId: Int, descId: Int, difficulty: Difficulty) {
        val levelNumber = currentLevel(this, difficulty)
        val preview = GameData.generateLevel(difficulty, levelNumber)
        findViewById<Button>(buttonId).text =
            "${difficulty.title} · Nivel $levelNumber/${GameData.MAX_LEVELS_PER_DIFFICULTY}"
        findViewById<TextView>(descId).text =
            "${difficulty.subtitle} ${preview.words.size} palabras · cuadrícula ${preview.gridSize}×${preview.gridSize}"

        findViewById<Button>(buttonId).setOnClickListener {
            val name = persistPlayerName() ?: return@setOnClickListener
            GameProgressStore.clearActiveSession(this)
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_PLAYER_NAME, name)
                putExtra(GameActivity.EXTRA_DIFFICULTY, difficulty.key)
                putExtra(GameActivity.EXTRA_LEVEL_NUMBER, levelNumber)
            })
        }
    }

    private fun persistPlayerName(): String? {
        val playerName = playerNameInput.text.toString().trim()
        if (playerName.isBlank()) {
            Toast.makeText(this, "Escribe tu nombre para comenzar.", Toast.LENGTH_SHORT).show()
            playerNameInput.requestFocus()
            return null
        }
        savePlayerName(this, playerName)
        return playerName
    }
}
