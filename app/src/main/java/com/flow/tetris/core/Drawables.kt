package com.flow.tetris.core

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.flow.tetris.core.extension.getColor
import com.flow.tetris.R

@SuppressLint("UseCompatLoadingForDrawables")
object Drawables {

    fun setBackground(
        view: View,
        tintColorId: Int = R.color.transparent,
        strokeColorId: Int = R.color.black,
        strokeWidth: Int = 1,
        radius: Float = 0f
    ) {
        val drawable = GradientDrawable().apply {
            setColor(tintColorId.getColor()) // 背景色
            setStroke(strokeWidth, strokeColorId.getColor()) // 枠線の幅と色
            cornerRadius = radius // 角の丸み
        }
        view.background = drawable
    }
 }