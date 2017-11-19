package com.digitalmischief.TTT3D.util

import android.content.Context
import android.widget.Toast

/**
 * Created by andy on 11/6/17.
 */
object Utils {

    fun showMessageShort(context: Context, msg: String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }
    fun showMessageLong(context: Context, msg: String){
        Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
    }

}