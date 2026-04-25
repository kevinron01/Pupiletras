package com.kevinron.pupiletras

import android.content.Context

object GameSettings {
    private const val PREFS_NAME = "pupiletras_prefs"
    private const val KEY_TIME_LIMIT_PREFIX = "time_limit_"

    private data class TimeRule(val minMinutes: Int, val maxMinutes: Int, val defaultMinutes: Int)

    private fun ruleFor(difficulty: Difficulty): TimeRule = when (difficulty) {
        Difficulty.EASY -> TimeRule(minMinutes = 1, maxMinutes = 8, defaultMinutes = 3)
        Difficulty.MEDIUM -> TimeRule(minMinutes = 2, maxMinutes = 10, defaultMinutes = 5)
        Difficulty.HARD -> TimeRule(minMinutes = 3, maxMinutes = 12, defaultMinutes = 7)
    }

    fun timeLimitMinutes(context: Context, difficulty: Difficulty): Int {
        val rule = ruleFor(difficulty)
        val stored = prefs(context).getInt(keyFor(difficulty), rule.defaultMinutes)
        return stored.coerceIn(rule.minMinutes, rule.maxMinutes)
    }

    fun saveTimeLimitMinutes(context: Context, difficulty: Difficulty, minutes: Int) {
        prefs(context).edit()
            .putInt(keyFor(difficulty), normalizeInputMinutes(difficulty, minutes))
            .apply()
    }

    fun formatTimeLimit(minutes: Int): String = "${minutes} min"
    fun rangeLabel(difficulty: Difficulty): String {
        val rule = ruleFor(difficulty)
        return "${rule.minMinutes}-${rule.maxMinutes} min"
    }

    fun normalizeInputMinutes(difficulty: Difficulty, rawValue: Int): Int {
        val rule = ruleFor(difficulty)
        return rawValue.coerceIn(rule.minMinutes, rule.maxMinutes)
    }

    private fun keyFor(difficulty: Difficulty) = "$KEY_TIME_LIMIT_PREFIX${difficulty.key}"

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
}
