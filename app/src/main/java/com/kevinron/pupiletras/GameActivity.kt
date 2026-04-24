package com.kevinron.pupiletras

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GameActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LEVEL_ID = "level_id"
    }

    private lateinit var level: LevelConfig
    private lateinit var adapter: WordAdapter
    private lateinit var progress: TextView
    private lateinit var chronometer: Chronometer
    private val found = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val levelId = intent.getIntExtra(EXTRA_LEVEL_ID, 1)
        level = GameData.levelById(levelId)

        findViewById<TextView>(R.id.tvLevelTitle).text = level.title
        findViewById<TextView>(R.id.tvCategory).text = level.category
        progress = findViewById(R.id.tvProgress)
        chronometer = findViewById(R.id.chronometer)
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener { finish() }

        adapter = WordAdapter(level.words)
        val list = findViewById<RecyclerView>(R.id.wordList)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = adapter

        val board = findViewById<WordSearchView>(R.id.wordSearch)
        board.setLevel(level)
        board.onWordFound = { word -> handleWordFound(word) }

        updateProgress()
    }

    private fun handleWordFound(word: String) {
        if (found.add(word)) {
            adapter.markFound(word)
            updateProgress()
            if (found.size == level.words.size) {
                chronometer.stop()
                val elapsedMs = SystemClock.elapsedRealtime() - chronometer.base
                startActivity(Intent(this, WinActivity::class.java).apply {
                    putExtra(WinActivity.EXTRA_LEVEL_ID, level.id)
                    putExtra(WinActivity.EXTRA_ELAPSED_MS, elapsedMs)
                })
                finish()
            }
        }
    }

    private fun updateProgress() {
        progress.text = "${found.size}/${level.words.size}"
    }
}
