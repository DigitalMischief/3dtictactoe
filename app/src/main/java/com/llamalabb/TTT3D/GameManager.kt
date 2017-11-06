package com.llamalabb.TTT3D

import android.util.Log
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.Cell
import com.llamalabb.TTT3D.models.CellType
import com.llamalabb.TTT3D.models.Position

/**
 * Created by brandon on 11/4/17.
 */

class GameManager(private val board: Board) {

    private val searchDistance = 1
    private val TAG = this.javaClass.simpleName

    private fun getAlliesInSearchArea(position: Position, type: CellType) {
        getNeighbors(position)
                .filter {it.type == type}
                .forEach{
                    Log.d(TAG, "Ally found at position: $position")
                }
    }

    private fun getNeighbors(position: Position): List<Cell> {
        val (X, Y) = position
        var cellList = mutableListOf<Cell>()
        (maxOf(0, X - searchDistance)..X + searchDistance).forEach { xPos ->
            (maxOf(0, Y - searchDistance)..Y + searchDistance)
                    .filter { yPos ->
                        !(xPos == X && yPos == Y)
                    }
                    .forEach { yPos ->
                        Log.d(TAG, "Scanning position - X:$xPos Y:$yPos")
                        cellList.add(board.getCell(Position(xPos, yPos)))
                    }
        }

        return cellList
    }
}