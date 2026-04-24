package com.kevinron.pupiletras

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class WordSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private data class Placement(val word: String, val cells: List<Pair<Int, Int>>)

    var onWordFound: ((String) -> Unit)? = null

    private var gridSize = 9
    private var grid: Array<CharArray> = Array(gridSize) { CharArray(gridSize) { ' ' } }
    private var placements: List<Placement> = emptyList()
    private val foundCells = mutableSetOf<Pair<Int, Int>>()
    private val foundWords = mutableSetOf<String>()

    private var cellSize = 0f
    private var selStart: Pair<Int, Int>? = null
    private var selEnd: Pair<Int, Int>? = null

    private val cellPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val cellStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E3EAF5")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val foundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#66A5D6A7")
        style = Paint.Style.FILL
    }
    private val selectionPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#661565C0")
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A237E")
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    fun setLevel(level: LevelConfig) {
        gridSize = level.gridSize
        foundCells.clear()
        foundWords.clear()
        generateBoard(level.words)
        requestLayout()
        invalidate()
    }

    private fun generateBoard(words: List<String>) {
        val directions = listOf(
            0 to 1, 1 to 0, 1 to 1, -1 to 1,
            0 to -1, -1 to 0, -1 to -1, 1 to -1
        )
        val rnd = java.util.Random()

        for (attempt in 0 until 50) {
            val board = Array(gridSize) { CharArray(gridSize) { ' ' } }
            val placed = mutableListOf<Placement>()
            var ok = true
            for (word in words.sortedByDescending { it.length }) {
                var placedWord = false
                for (tryN in 0 until 200) {
                    val (dr, dc) = directions[rnd.nextInt(directions.size)]
                    val r0 = rnd.nextInt(gridSize)
                    val c0 = rnd.nextInt(gridSize)
                    val cells = mutableListOf<Pair<Int, Int>>()
                    var valid = true
                    for (i in word.indices) {
                        val r = r0 + dr * i
                        val c = c0 + dc * i
                        if (r !in 0 until gridSize || c !in 0 until gridSize) {
                            valid = false; break
                        }
                        val existing = board[r][c]
                        if (existing != ' ' && existing != word[i]) {
                            valid = false; break
                        }
                        cells.add(r to c)
                    }
                    if (valid) {
                        for ((i, cell) in cells.withIndex()) {
                            board[cell.first][cell.second] = word[i]
                        }
                        placed.add(Placement(word, cells))
                        placedWord = true
                        break
                    }
                }
                if (!placedWord) { ok = false; break }
            }
            if (ok) {
                for (r in 0 until gridSize) {
                    for (c in 0 until gridSize) {
                        if (board[r][c] == ' ') {
                            board[r][c] = ('A' + rnd.nextInt(26))
                        }
                    }
                }
                grid = board
                placements = placed
                return
            }
        }
        // Fallback: fill random letters
        grid = Array(gridSize) { CharArray(gridSize) { ('A' + rnd.nextInt(26)) } }
        placements = emptyList()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        val side = min(w, h)
        setMeasuredDimension(side, side)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        cellSize = min(w, h).toFloat() / gridSize
        textPaint.textSize = cellSize * 0.55f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (cellSize == 0f) return
        val rect = RectF()
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val left = c * cellSize
                val top = r * cellSize
                rect.set(left + 1, top + 1, left + cellSize - 1, top + cellSize - 1)
                canvas.drawRoundRect(rect, 6f, 6f, cellPaint)
                canvas.drawRoundRect(rect, 6f, 6f, cellStroke)
                if (foundCells.contains(r to c)) {
                    canvas.drawRoundRect(rect, 6f, 6f, foundPaint)
                }
            }
        }
        val selection = currentSelection()
        for ((r, c) in selection) {
            val left = c * cellSize
            val top = r * cellSize
            rect.set(left + 1, top + 1, left + cellSize - 1, top + cellSize - 1)
            canvas.drawRoundRect(rect, 6f, 6f, selectionPaint)
        }
        val baseline = (textPaint.descent() + textPaint.ascent()) / 2
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                val cx = c * cellSize + cellSize / 2
                val cy = r * cellSize + cellSize / 2 - baseline
                canvas.drawText(grid[r][c].toString(), cx, cy, textPaint)
            }
        }
    }

    private fun currentSelection(): List<Pair<Int, Int>> {
        val s = selStart ?: return emptyList()
        val e = selEnd ?: return listOf(s)
        return lineCells(s, e)
    }

    private fun lineCells(a: Pair<Int, Int>, b: Pair<Int, Int>): List<Pair<Int, Int>> {
        val dr = b.first - a.first
        val dc = b.second - a.second
        val adr = abs(dr); val adc = abs(dc)
        val length = max(adr, adc)
        if (length == 0) return listOf(a)
        val straight = dr == 0 || dc == 0 || adr == adc
        if (!straight) return emptyList()
        val sr = if (dr == 0) 0 else dr / adr
        val sc = if (dc == 0) 0 else dc / adc
        return (0..length).map { i -> (a.first + sr * i) to (a.second + sc * i) }
    }

    private fun cellAt(x: Float, y: Float): Pair<Int, Int>? {
        if (cellSize == 0f) return null
        val c = (x / cellSize).toInt()
        val r = (y / cellSize).toInt()
        if (r !in 0 until gridSize || c !in 0 until gridSize) return null
        return r to c
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                selStart = cellAt(event.x, event.y)
                selEnd = selStart
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val c = cellAt(event.x, event.y) ?: return true
                selEnd = c
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                commitSelection()
                selStart = null
                selEnd = null
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun commitSelection() {
        val cells = currentSelection()
        if (cells.size < 2) return
        val word = cells.map { grid[it.first][it.second] }.joinToString("")
        val reversed = word.reversed()
        val target = placements.firstOrNull {
            !foundWords.contains(it.word) &&
                (it.word == word || it.word == reversed) &&
                (it.cells == cells || it.cells == cells.reversed())
        } ?: return
        foundWords.add(target.word)
        foundCells.addAll(target.cells)
        onWordFound?.invoke(target.word)
    }
}
