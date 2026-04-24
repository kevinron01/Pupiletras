package com.kevinron.pupiletras

import android.content.Context

data class SavedGameState(
    val playerName: String,
    val difficulty: Difficulty,
    val levelNumber: Int,
    val sessionSeed: Int,
    val elapsedMs: Long,
    val score: Int,
    val foundWords: Set<String>
)

object GameProgressStore {
    private const val PREFS_NAME = "pupiletras_prefs"
    private const val KEY_TOTAL_SCORE = "total_score"
    private const val KEY_ACTIVE_PLAYER = "active_player"
    private const val KEY_ACTIVE_DIFFICULTY = "active_difficulty"
    private const val KEY_ACTIVE_LEVEL = "active_level"
    private const val KEY_ACTIVE_SEED = "active_seed"
    private const val KEY_ACTIVE_ELAPSED = "active_elapsed"
    private const val KEY_ACTIVE_SCORE = "active_score"
    private const val KEY_ACTIVE_FOUND = "active_found"

    fun saveActiveSession(context: Context, state: SavedGameState) {
        prefs(context).edit()
            .putString(KEY_ACTIVE_PLAYER, state.playerName)
            .putString(KEY_ACTIVE_DIFFICULTY, state.difficulty.key)
            .putInt(KEY_ACTIVE_LEVEL, state.levelNumber)
            .putInt(KEY_ACTIVE_SEED, state.sessionSeed)
            .putLong(KEY_ACTIVE_ELAPSED, state.elapsedMs)
            .putInt(KEY_ACTIVE_SCORE, state.score)
            .putString(KEY_ACTIVE_FOUND, state.foundWords.joinToString("|"))
            .apply()
    }

    fun loadActiveSession(context: Context): SavedGameState? {
        val prefs = prefs(context)
        val difficultyKey = prefs.getString(KEY_ACTIVE_DIFFICULTY, null) ?: return null
        val playerName = prefs.getString(KEY_ACTIVE_PLAYER, null) ?: return null
        val levelNumber = prefs.getInt(KEY_ACTIVE_LEVEL, 0)
        if (levelNumber <= 0) return null

        val foundWords = prefs.getString(KEY_ACTIVE_FOUND, "")
            .orEmpty()
            .split("|")
            .filter { it.isNotBlank() }
            .toSet()

        return SavedGameState(
            playerName = playerName,
            difficulty = Difficulty.fromKey(difficultyKey),
            levelNumber = levelNumber,
            sessionSeed = prefs.getInt(KEY_ACTIVE_SEED, 0),
            elapsedMs = prefs.getLong(KEY_ACTIVE_ELAPSED, 0L),
            score = prefs.getInt(KEY_ACTIVE_SCORE, 0),
            foundWords = foundWords
        )
    }

    fun clearActiveSession(context: Context) {
        prefs(context).edit()
            .remove(KEY_ACTIVE_PLAYER)
            .remove(KEY_ACTIVE_DIFFICULTY)
            .remove(KEY_ACTIVE_LEVEL)
            .remove(KEY_ACTIVE_SEED)
            .remove(KEY_ACTIVE_ELAPSED)
            .remove(KEY_ACTIVE_SCORE)
            .remove(KEY_ACTIVE_FOUND)
            .apply()
    }

    fun totalScore(context: Context): Int = prefs(context).getInt(KEY_TOTAL_SCORE, 0)

    fun addToTotalScore(context: Context, value: Int): Int {
        val newTotal = totalScore(context) + value
        prefs(context).edit().putInt(KEY_TOTAL_SCORE, newTotal).apply()
        return newTotal
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
