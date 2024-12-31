package com.flow.tetris.presentation.play

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flow.tetris.R
import com.flow.tetris.core.Application
import com.flow.tetris.core.extension.getColor
import com.flow.tetris.databinding.ListItemTetrisBinding
import com.flow.tetris.core.BlockType
import com.flow.tetris.core.Drawables
import com.flow.tetris.core.Tetris
import com.flow.tetris.core.currentType

class TetrisViewHolder(
    private val binding: ListItemTetrisBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun update(id: Int) {
        val type = BlockType.ordinalOf(id)
        if (type == BlockType.FALL) {
            binding.view.setBackgroundColor(currentType.colorId.getColor())
        } else if (type != BlockType.GHOST) {
            binding.view.setBackgroundColor(type.colorId.getColor())
        } else {
            Drawables.setBackground(
                view = binding.view,
                strokeColorId = type.colorId
            )
        }
    }
}