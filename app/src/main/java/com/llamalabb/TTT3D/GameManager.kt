package com.llamalabb.TTT3D

import android.util.Log
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.Cell
import com.llamalabb.TTT3D.models.Position

/**
 * Created by brandon on 11/4/17.
 */

class GameManager(private val board: Board) {

    private val searchDistance = 1
    private val TAG = this.javaClass.simpleName

    fun getNeighbors(position: Position) : List<Cell>{
        val (X, Y) = position
        val startX = X - searchDistance
        val startY = Y - searchDistance
        val endX = X + searchDistance
        val endY = Y + searchDistance
        if (searchDistance > 0) {
            for (xPos in maxOf(0, startX)..endX) {
                (maxOf(0, startY)..endY)
                        .filter { yPos ->
                            !(xPos == X && yPos == Y)
                        }
                        .forEach { yPos ->
                            Log.d(TAG, "Scanning position - X:$xPos Y:$yPos")
                            board.getCell(Position(xPos, yPos))
                        }
            }
        }
        return emptyList()
    }
}