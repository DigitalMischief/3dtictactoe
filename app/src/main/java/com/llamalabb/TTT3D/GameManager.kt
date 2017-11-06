package com.llamalabb.TTT3D

import android.util.Log
import com.llamalabb.TTT3D.models.*

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

    private fun getDirectionOfPositionFromPosition(thisPosition: Position, otherPosition: Position) : Direction {
       val (thisX, thisY) = thisPosition
       val (otherX, otherY) = otherPosition
        return when {
            otherY == thisY -> when {
                otherX > thisX -> Direction.East
                else -> Direction.West
            }
            otherY > thisY -> //We're in the north
                when{
                    otherX > thisX -> Direction.NorthEast
                    otherX < thisX -> Direction.NorthWest
                    else -> Direction.North
                }
            else -> //We're in the south
                when{
                    otherX > thisX -> Direction.SouthEast
                    otherX < thisX -> Direction.SouthWest
                    else -> Direction.South
                }
        }
    }
}