package com.kevinron.pupiletras

import androidx.annotation.ColorRes
import java.text.Normalizer
import kotlin.math.min
import kotlin.random.Random

enum class Difficulty(
    val key: String,
    val title: String,
    val subtitle: String,
    @ColorRes val colorRes: Int,
    val baseGridSize: Int,
    val maxGridSize: Int,
    val baseWordCount: Int
) {
    EASY(
        key = "easy",
        title = "Fácil",
        subtitle = "Palabras cortas para aprender jugando.",
        colorRes = R.color.level1,
        baseGridSize = 8,
        maxGridSize = 12,
        baseWordCount = 5
    ),
    MEDIUM(
        key = "medium",
        title = "Media",
        subtitle = "Más palabras y significados más ricos.",
        colorRes = R.color.level2,
        baseGridSize = 10,
        maxGridSize = 14,
        baseWordCount = 6
    ),
    HARD(
        key = "hard",
        title = "Difícil",
        subtitle = "Vocabulario amplio para sesiones largas.",
        colorRes = R.color.level3,
        baseGridSize = 12,
        maxGridSize = 16,
        baseWordCount = 7
    );

    companion object {
        fun fromKey(key: String?): Difficulty = entries.firstOrNull { it.key == key } ?: EASY
    }
}

data class VocabularyWord(
    val text: String,
    val definition: String
) {
    val boardText: String = normalize(text)

    companion object {
        private fun normalize(value: String): String {
            val normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            return normalized.uppercase()
        }
    }
}

data class LevelConfig(
    val levelNumber: Int,
    val difficulty: Difficulty,
    val title: String,
    val topicLabel: String,
    val gridSize: Int,
    val words: List<VocabularyWord>,
    val boardSeed: Int
)

object GameData {
    const val MAX_LEVELS_PER_DIFFICULTY = 50
    private val vocabulary = VocabularyBank.allWords()

    fun generateLevel(
        difficulty: Difficulty,
        levelNumber: Int,
        sessionSeed: Int = 0
    ): LevelConfig {
        val safeLevel = levelNumber.coerceIn(1, MAX_LEVELS_PER_DIFFICULTY)
        val levelSeed = difficulty.key.hashCode() * 31 + safeLevel * 17 + sessionSeed
        val random = Random(levelSeed)

        val gridGrowth = (safeLevel - 1) / 2
        val gridSize = min(difficulty.baseGridSize + gridGrowth, difficulty.maxGridSize)
        val desiredWordCount = min(
            difficulty.baseWordCount + ((safeLevel - 1) / 2) + 1,
            vocabulary.size.coerceAtMost(gridSize + 2)
        )

        val maxLength = when (difficulty) {
            Difficulty.EASY -> min(gridSize, 7)
            Difficulty.MEDIUM -> min(gridSize, 10)
            Difficulty.HARD -> gridSize
        }

        val minLength = when (difficulty) {
            Difficulty.EASY -> 4
            Difficulty.MEDIUM -> 5
            Difficulty.HARD -> 6
        }

        val candidates = vocabulary
            .distinctBy { it.boardText }
            .filter { it.boardText.length in minLength..maxLength }

        val selectedWords = candidates
            .shuffled(random)
            .take(desiredWordCount)
            .sortedBy { it.boardText.length }

        return LevelConfig(
            levelNumber = safeLevel,
            difficulty = difficulty,
            title = "Nivel $safeLevel · ${difficulty.title}",
            topicLabel = "Vocabulario mixto",
            gridSize = gridSize,
            words = selectedWords,
            boardSeed = random.nextInt()
        )
    }
}
