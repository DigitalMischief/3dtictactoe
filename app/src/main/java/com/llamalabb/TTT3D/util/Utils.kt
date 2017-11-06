package com.llamalabb.TTT3D.util

import android.app.Application
import android.content.Context
import android.widget.Toast

/**
 * Created by andy on 11/6/17.
 */
object Utils : Application() {

    fun showMessageShort(msg: String){
        Toast.makeText(applicationContext,msg, Toast.LENGTH_SHORT).show()
    }
    fun showMessageLong(msg: String){
        Toast.makeText(applicationContext,msg, Toast.LENGTH_LONG).show()
    }

}