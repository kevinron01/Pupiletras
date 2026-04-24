package com.kevinron.pupiletras

data class LevelConfig(
    val id: Int,
    val title: String,
    val category: String,
    val gridSize: Int,
    val words: List<String>
)

object GameData {
    val levels = listOf(
        LevelConfig(
            id = 1,
            title = "Nivel 1 — Fácil",
            category = "Animales",
            gridSize = 9,
            words = listOf("GATO", "PERRO", "LEON", "TIGRE", "OSO", "LOBO")
        ),
        LevelConfig(
            id = 2,
            title = "Nivel 2 — Medio",
            category = "Frutas",
            gridSize = 11,
            words = listOf("MANZANA", "PERA", "UVA", "MANGO", "SANDIA", "FRESA", "LIMON")
        ),
        LevelConfig(
            id = 3,
            title = "Nivel 3 — Difícil",
            category = "Países",
            gridSize = 13,
            words = listOf(
                "MEXICO", "ESPAÑA", "ARGENTINA", "COLOMBIA",
                "PERU", "CHILE", "BRASIL", "URUGUAY"
            )
        )
    )

    fun levelById(id: Int): LevelConfig = levels.first { it.id == id }
    fun nextLevelId(id: Int): Int? = levels.firstOrNull { it.id == id + 1 }?.id
}
