package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Cell(val position: Position, var type: CellType? = null) {

    fun scanNeighbors() {

    }

    fun runCheck() {

    }

    fun getTypeAsString() : String{
        return when (type) {
            CellType.X -> "X"
            CellType.O -> "O"
            else -> ""
        }
    }

    fun isAvailable() =  type == null

    fun handleClickByPlayer(){
        if (isAvailable()){
        }
    }


}