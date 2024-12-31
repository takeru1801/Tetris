package com.flow.tetris.presentation.play

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.RecyclerView
import com.flow.tetris.databinding.ListItemTetrisBinding
import com.flow.tetris.core.Tetris

class PlayAdapter(
    private val list: Array<Array<Int>>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ListItemTetrisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TetrisViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return Tetris.ROW * Tetris.COL
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TetrisViewHolder -> {
                // アイテムのサイズを計算
                val id = list[position/Tetris.COL][position%Tetris.COL]
                holder.update(id)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update() {
        notifyDataSetChanged()
    }
}