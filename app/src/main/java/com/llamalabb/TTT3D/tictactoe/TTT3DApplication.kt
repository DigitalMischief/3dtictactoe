package com.llamalabb.TTT3D.tictactoe

import android.app.Application
import com.llamalabb.TTT3D.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree



/**
 * Created by brandon on 11/6/17.
 */
class TTT3DApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}