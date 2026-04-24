package com.kevinron.pupiletras

import android.content.Context

object GameSettings {
    private const val PREFS_NAME = "pupiletras_prefs"
    private const val KEY_TIME_LIMIT_PREFIX = "time_limit_"

    private val easyOptionsMinutes = listOf(2, 3, 4, 5)
    private val mediumOptionsMinutes = listOf(3, 4, 5, 6)
    private val hardOptionsMinutes = listOf(4, 5, 6, 8)

    fun optionsFor(difficulty: Difficulty): List<Int> = when (difficulty) {
        Difficulty.EASY -> easyOptionsMinutes
        Difficulty.MEDIUM -> mediumOptionsMinutes
        Difficulty.HARD -> hardOptionsMinutes
    }

    fun timeLimitMinutes(context: Context, difficulty: Difficulty): Int {
        val options = optionsFor(difficulty)
        val default = options.first()
        val stored = prefs(context).getInt(keyFor(difficulty), default)
        return if (options.contains(stored)) stored else default
    }

    fun saveTimeLimitMinutes(context: Context, difficulty: Difficulty, minutes: Int) {
        val safeValue = if (optionsFor(difficulty).contains(minutes)) {
            minutes
        } else {
            optionsFor(difficulty).first()
        }
        prefs(context).edit().putInt(keyFor(difficulty), safeValue).apply()
    }

    fun cycleTimeLimitMinutes(context: Context, difficulty: Difficulty): Int {
        val options = optionsFor(difficulty)
        val current = timeLimitMinutes(context, difficulty)
        val nextIndex = (options.indexOf(current) + 1).mod(options.size)
        val next = options[nextIndex]
        saveTimeLimitMinutes(context, difficulty, next)
        return next
    }

    fun formatTimeLimit(minutes: Int): String = "${minutes} min"

    private fun keyFor(difficulty: Difficulty) = "$KEY_TIME_LIMIT_PREFIX${difficulty.key}"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
