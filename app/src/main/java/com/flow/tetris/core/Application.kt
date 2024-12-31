package com.flow.tetris.core

import android.app.Application
import android.content.Context

class Application: Application() {

    init {
        instance = this
    }

    companion object {
        lateinit var instance: Application

        val context: Context
            get() = instance.applicationContext
    }

}