package com.kevinron.pupiletras

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WordAdapter(
    private val words: List<VocabularyWord>,
    private val onWordSelected: (VocabularyWord) -> Unit
) : RecyclerView.Adapter<WordAdapter.Holder>() {

    private val foundWords = mutableSetOf<String>()
    private var selectedWord = words.firstOrNull()?.boardText

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.wordText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val word = words[position]
        val isFound = foundWords.contains(word.boardText)
        val isSelected = selectedWord == word.boardText

        holder.text.text = word.text
        if (isFound) {
            holder.text.paintFlags = holder.text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.text.setTextColor(
                ContextCompat.getColor(holder.text.context, R.color.text_secondary)
            )
            holder.text.alpha = 0.6f
        } else {
            holder.text.paintFlags = holder.text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.text.setTextColor(
                ContextCompat.getColor(holder.text.context, R.color.primary)
            )
            holder.text.alpha = 1f
        }

        holder.itemView.alpha = if (isSelected) 1f else 0.82f
        holder.itemView.scaleX = if (isSelected) 1.04f else 1f
        holder.itemView.scaleY = if (isSelected) 1.04f else 1f
        holder.itemView.setOnClickListener {
            val previous = selectedWord
            selectedWord = word.boardText
            previous?.let { previousWord ->
                val previousIndex = words.indexOfFirst { it.boardText == previousWord }
                if (previousIndex >= 0) notifyItemChanged(previousIndex)
            }
            notifyItemChanged(position)
            onWordSelected(word)
        }
    }

    override fun getItemCount() = words.size

    fun markFound(word: String) {
        if (foundWords.add(word)) {
            val idx = words.indexOfFirst { it.boardText == word }
            if (idx >= 0) notifyItemChanged(idx)
        }
    }

    fun restoreFound(wordsToRestore: Set<String>) {
        foundWords.clear()
        foundWords.addAll(wordsToRestore)
        notifyDataSetChanged()
    }
}
