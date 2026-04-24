package com.kevinron.pupiletras

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLAYER_NAME = "player_name"
        const val EXTRA_DIFFICULTY = "difficulty"
        const val EXTRA_LEVEL_NUMBER = "level_number"
        const val EXTRA_SESSION_SEED = "session_seed"
        const val EXTRA_RESUME_SAVED_GAME = "resume_saved_game"
        private const val HINT_COST = 18
        private const val SURPRISE_COST = 30
        private const val HINT_DURATION_MS = 2200L
        private const val SURPRISE_DURATION_MS = 3500L
    }

    private lateinit var level: LevelConfig
    private lateinit var playerName: String
    private lateinit var adapter: WordAdapter
    private lateinit var progress: TextView
    private lateinit var chronometer: Chronometer
    private lateinit var definitionTitle: TextView
    private lateinit var definitionText: TextView
    private lateinit var scoreView: TextView
    private lateinit var cheerView: TextView
    private lateinit var definitionCard: LinearLayout
    private lateinit var board: WordSearchView

    private val found = mutableSetOf<String>()
    private val surpriseMessages = listOf(
        "sigue buscando, vas excelente",
        "tu vocabulario está creciendo",
        "excelente trabajo",
        "cada palabra te hace más fuerte",
        "vamos, tú puedes",
        "qué gran explorador de palabras"
    )
    private var score = 0
    private var sessionSeed = 0
    private var restoredElapsedMs = 0L
    private var isCompleting = false
    private var clearHintRunnable: Runnable? = null
    private var maxTimeMs: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val saved = if (intent.getBooleanExtra(EXTRA_RESUME_SAVED_GAME, false)) {
            GameProgressStore.loadActiveSession(this)
        } else {
            null
        }

        playerName = saved?.playerName
            ?: intent.getStringExtra(EXTRA_PLAYER_NAME)
            ?.takeIf { it.isNotBlank() }
            ?: MainActivity.playerName(this)
            .ifBlank { "Jugador" }

        val difficulty = saved?.difficulty
            ?: Difficulty.fromKey(intent.getStringExtra(EXTRA_DIFFICULTY))
        val levelNumber = saved?.levelNumber ?: intent.getIntExtra(EXTRA_LEVEL_NUMBER, 1)
        sessionSeed = saved?.sessionSeed ?: intent.getIntExtra(EXTRA_SESSION_SEED, Random.nextInt())
        restoredElapsedMs = saved?.elapsedMs ?: 0L
        score = saved?.score ?: 0
        found.clear()
        found.addAll(saved?.foundWords.orEmpty())
        MainActivity.savePlayerName(this, playerName)

        level = GameData.generateLevel(difficulty, levelNumber, sessionSeed)
        maxTimeMs = GameSettings.timeLimitMinutes(this, level.difficulty) * 60_000L

        findViewById<TextView>(R.id.tvLevelTitle).text = level.title
        findViewById<TextView>(R.id.tvCategory).text =
            "${level.topicLabel} · ${difficulty.title} · tiempo: ${GameSettings.formatTimeLimit((maxTimeMs / 60_000L).toInt())}"
        findViewById<TextView>(R.id.tvPlayer).text = "Jugador: $playerName"

        progress = findViewById(R.id.tvProgress)
        chronometer = findViewById(R.id.chronometer)
        definitionTitle = findViewById(R.id.tvDefinitionTitle)
        definitionText = findViewById(R.id.tvDefinitionText)
        scoreView = findViewById(R.id.tvScore)
        cheerView = findViewById(R.id.tvCheer)
        definitionCard = findViewById(R.id.definitionCard)

        chronometer.base = SystemClock.elapsedRealtime() - restoredElapsedMs
        chronometer.setOnChronometerTickListener {
            if (!isCompleting) {
                val elapsedMs = SystemClock.elapsedRealtime() - chronometer.base
                if (elapsedMs >= maxTimeMs) {
                    handleTimeExpired()
                }
            }
        }
        chronometer.start()

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }
        findViewById<Button>(R.id.btnHint).setOnClickListener { showHint() }
        findViewById<Button>(R.id.btnSurprise).setOnClickListener { showSurprise() }
        definitionCard.setOnClickListener { cycleDefinition() }

        adapter = WordAdapter(level.words) { word -> showDefinition(word) }
        val list = findViewById<RecyclerView>(R.id.wordList)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = adapter
        adapter.restoreFound(found)

        board = findViewById(R.id.wordSearch)
        board.setLevel(level, found)
        board.onWordFound = { word -> handleWordFound(word) }

        updateProgress()
        updateScore()
        level.words.firstOrNull()?.let { showDefinition(it) }
        if (saved != null) {
            showCheer("¡$playerName, seguimos donde te quedaste!")
        } else {
            showCheer("¡$playerName, encuentra palabras y gana estrellas!")
        }
    }

    override fun onPause() {
        super.onPause()
        if (!isCompleting) {
            saveSession()
        }
    }

    override fun onStop() {
        super.onStop()
        if (!isCompleting) {
            saveSession()
        }
    }

    private fun handleWordFound(word: String) {
        if (found.add(word)) {
            adapter.markFound(word)
            val gainedPoints = calculateWordScore(word)
            score += gainedPoints
            updateScore()
            animateScore()
            showCheer("¡$playerName, ${surpriseMessages.random()}! +$gainedPoints puntos")
            level.words.firstOrNull { it.boardText == word }?.let { showDefinition(it) }
            updateProgress()
            if (found.size == level.words.size) {
                completeLevel()
            } else {
                saveSession()
            }
        }
    }

    private fun completeLevel() {
        chronometer.stop()
        isCompleting = true
        val elapsedMs = SystemClock.elapsedRealtime() - chronometer.base
        val completionBonus = 50 + (level.levelNumber * 10)
        score += completionBonus
        updateScore()
        MainActivity.unlockNextLevel(this, level.difficulty, level.levelNumber)
        GameProgressStore.clearActiveSession(this)
        val totalScore = GameProgressStore.addToTotalScore(this, score)
        startActivity(Intent(this, WinActivity::class.java).apply {
            putExtra(WinActivity.EXTRA_PLAYER_NAME, playerName)
            putExtra(WinActivity.EXTRA_DIFFICULTY, level.difficulty.key)
            putExtra(WinActivity.EXTRA_LEVEL_NUMBER, level.levelNumber)
            putExtra(WinActivity.EXTRA_ELAPSED_MS, elapsedMs)
            putExtra(WinActivity.EXTRA_SCORE, score)
            putExtra(WinActivity.EXTRA_TOTAL_SCORE, totalScore)
            putExtra(WinActivity.EXTRA_COMPLETION_BONUS, completionBonus)
        })
        finish()
    }

    private fun handleTimeExpired() {
        if (isCompleting) return
        isCompleting = true
        chronometer.stop()
        clearHintRunnable?.let { board.removeCallbacks(it) }
        clearHintRunnable = null
        board.clearHint()
        GameProgressStore.clearActiveSession(this)
        showCheer("¡$playerName! Se acabó el tiempo en ${level.difficulty.title}. Inténtalo de nuevo.")
        Toast.makeText(
            this,
            "Tiempo agotado (${GameSettings.formatTimeLimit((maxTimeMs / 60_000L).toInt())})",
            Toast.LENGTH_LONG
        ).show()
        board.postDelayed({ finish() }, 1000L)
    }

    private fun updateProgress() {
        progress.text = "${found.size}/${level.words.size}"
    }

    private fun updateScore() {
        scoreView.text = "$score"
    }

    private fun showDefinition(word: VocabularyWord) {
        definitionTitle.text = word.text
        definitionText.text = word.definition
        pulseDefinitionCard()
    }

    private fun calculateWordScore(word: String): Int {
        val difficultyBonus = when (level.difficulty) {
            Difficulty.EASY -> 8
            Difficulty.MEDIUM -> 12
            Difficulty.HARD -> 18
        }
        return word.length * 10 + difficultyBonus
    }

    private fun animateScore() {
        scoreView.animate().cancel()
        scoreView.scaleX = 1f
        scoreView.scaleY = 1f
        scoreView.animate()
            .scaleX(1.18f)
            .scaleY(1.18f)
            .setDuration(160)
            .withEndAction {
                scoreView.animate().scaleX(1f).scaleY(1f).setDuration(140).start()
            }
            .start()
    }

    private fun showCheer(message: String) {
        cheerView.text = message
        cheerView.alpha = 0f
        cheerView.translationY = 12f
        cheerView.animate().alpha(1f).translationY(0f).setDuration(200).start()
    }

    private fun showHint() {
        val remaining = level.words.filterNot { found.contains(it.boardText) }
        if (remaining.isEmpty()) {
            showCheer("¡$playerName, ya encontraste todas!")
            return
        }
        val word = remaining.random()
        showDefinition(word)
        applyAidCost(HINT_COST)
        val hasHintOnBoard = board.highlightHintForWord(
            word.boardText,
            WordSearchView.HintReveal.START
        )
        val position = board.hintStartPosition(word.boardText)
        val hintMessage = if (hasHintOnBoard) {
            scheduleHintClear(delayMs = HINT_DURATION_MS)
            if (position != null) {
                "Pista: ${word.text} inicia cerca de fila ${position.row}, columna ${position.col}"
            } else {
                "Pista: mira las casillas amarillas para ubicar ${word.text}"
            }
        } else {
            "Pista: ${word.text} empieza por ${word.text.first()}"
        }
        showCheer("¡$playerName! $hintMessage. -$HINT_COST puntos")
        saveSession()
    }

    private fun showSurprise() {
        val remaining = level.words.filterNot { found.contains(it.boardText) }
        if (remaining.isEmpty()) {
            showCheer("¡$playerName, ya encontraste todas!")
            return
        }
        val randomWord = remaining.random()
        showDefinition(randomWord)
        applyAidCost(SURPRISE_COST)
        if (board.highlightHintForWord(randomWord.boardText, WordSearchView.HintReveal.FULL)) {
            scheduleHintClear(delayMs = SURPRISE_DURATION_MS)
            showCheer("¡$playerName! Sorpresa: la palabra ${randomWord.text} fue señalada. ¡Márcala! -$SURPRISE_COST puntos")
        } else {
            showCheer("¡$playerName! Sorpresa: busca ${randomWord.text}. -$SURPRISE_COST puntos")
        }
        saveSession()
    }

    private fun applyAidCost(cost: Int) {
        score = (score - cost).coerceAtLeast(0)
        updateScore()
        animateScore()
    }

    private fun scheduleHintClear(delayMs: Long) {
        clearHintRunnable?.let { board.removeCallbacks(it) }
        clearHintRunnable = Runnable {
            board.clearHint()
            clearHintRunnable = null
        }
        board.postDelayed(clearHintRunnable, delayMs)
    }

    private fun cycleDefinition() {
        if (level.words.isEmpty()) return
        val currentTitle = definitionTitle.text.toString()
        val currentIndex = level.words.indexOfFirst { it.text == currentTitle }
        val nextIndex = if (currentIndex == -1) 0 else (currentIndex + 1) % level.words.size
        showDefinition(level.words[nextIndex])
        showCheer("¡$playerName, toca otra palabra o sigue buscando!")
    }

    private fun pulseDefinitionCard() {
        definitionCard.animate().cancel()
        definitionCard.scaleX = 1f
        definitionCard.scaleY = 1f
        definitionCard.animate()
            .scaleX(1.03f)
            .scaleY(1.03f)
            .setDuration(140)
            .withEndAction {
                definitionCard.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
            }
            .start()
    }

    private fun saveSession() {
        val elapsedMs = SystemClock.elapsedRealtime() - chronometer.base
        GameProgressStore.saveActiveSession(
            this,
            SavedGameState(
                playerName = playerName,
                difficulty = level.difficulty,
                levelNumber = level.levelNumber,
                sessionSeed = sessionSeed,
                elapsedMs = elapsedMs,
                score = score,
                foundWords = found
            )
        )
    }

    override fun onDestroy() {
        if (::board.isInitialized) {
            clearHintRunnable?.let { board.removeCallbacks(it) }
        }
        clearHintRunnable = null
        super.onDestroy()
    }
}
