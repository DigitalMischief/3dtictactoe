package com.digitalmischief.TTT3D

/**
 * Created by andy on 11/20/17.
 */
import android.content.ContentValues.TAG
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset

import org.json.JSONException
import org.json.JSONObject

import android.util.Log


/**
 * Basic turn data. It's just a blank data string and a turn number counter.
 *
 * @author wolff
 */
class TicTacToeTurn {

    var data = ""
    var turnCounter: Int = 0
    val TAG = "EBTurn"
    // This is the byte array we will write out to the TBMP API.
    fun persist(): ByteArray {
        val retVal = JSONObject()

        try {
            retVal.put("data", data)
            retVal.put("turnCounter", turnCounter)

        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        val st = retVal.toString()

        Log.d(TAG, "==== PERSISTING\n" + st)

        return st.toByteArray(Charset.forName("UTF-8"))
    }




    // Creates a new instance of TicTacToeTurn.
    fun unpersist(byteArray: ByteArray?): TicTacToeTurn? {

        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.")
            return TicTacToeTurn()
        }

        var st: String? = null
        var str = "UTF-8"
        try {
            st = String(byteArray, Charsets.UTF_8)
        } catch (e1: UnsupportedEncodingException) {
            e1.printStackTrace()
            return null
        }

        Log.d(TAG, "====UNPERSIST \n" + st)

        val retVal = TicTacToeTurn()

        try {
            val obj = JSONObject(st)

            if (obj.has("data")) {
                retVal.data = obj.getString("data")
            }
            if (obj.has("turnCounter")) {
                retVal.turnCounter = obj.getInt("turnCounter")
            }

        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return retVal
    }
}