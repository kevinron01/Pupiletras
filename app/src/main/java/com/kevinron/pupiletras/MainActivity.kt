package com.kevinron.pupiletras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindLevel(R.id.btnLevel1, R.id.tvDesc1, 1)
        bindLevel(R.id.btnLevel2, R.id.tvDesc2, 2)
        bindLevel(R.id.btnLevel3, R.id.tvDesc3, 3)
    }

    private fun bindLevel(buttonId: Int, descId: Int, levelId: Int) {
        val level = GameData.levelById(levelId)
        findViewById<TextView>(descId).text =
            "${level.words.size} palabras · cuadrícula ${level.gridSize}×${level.gridSize}"
        findViewById<Button>(buttonId).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java).apply {
                putExtra(GameActivity.EXTRA_LEVEL_ID, levelId)
            })
        }
    }
}
