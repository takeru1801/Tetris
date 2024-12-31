package com.flow.tetris.core.extension

import androidx.annotation.StringRes
import com.flow.tetris.core.Application

fun @receiver:StringRes Int.getString(): String {
    return Application.context.getString(this)
}

fun @receiver:StringRes Int.getString(vararg formatArgs: Any?): String {
    val args = formatArgs.map { it }.toTypedArray()
    return Application.context.getString(this, *args)
}

fun Int.formatMoney(): String {
    return "%,d".format(this)
}