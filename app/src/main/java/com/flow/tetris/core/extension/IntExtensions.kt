package com.flow.tetris.core.extension

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.flow.tetris.core.Application

@SuppressLint("UseCompatLoadingForDrawables")
fun @receiver:DrawableRes Int.getDrawable(): Drawable? {
    return Application.context.getDrawable(this)
}

fun @receiver:ColorRes Int.getColor(): Int {
    return ContextCompat.getColor(Application.context, this)//Application.context.getColor(this)
}