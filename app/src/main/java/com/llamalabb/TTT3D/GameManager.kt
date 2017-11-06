package com.llamalabb.TTT3D

import com.llamalabb.TTT3D.models.*
import timber.log.Timber

/**
 * Created by brandon on 11/4/17.
 */

class GameManager(private val board: Board) {

    private val searchDistance = 1

    fun getAlliesInSearchArea(position: Position, type: CellType) {
        getNeighbors(position)
                .filter { it.type == type }
                .forEach {
                    val direction = getDirectionOfPositionFromPosition(position, it.position)
                    Timber.d("Ally found in the ${direction.javaClass.simpleName} position: ${it.position}")
                }
    }

    private fun getNeighbors(position: Position): List<Cell> {
        val (X, Y) = position
        val cellList = mutableListOf<Cell>()
        (maxOf(0, X - searchDistance)..minOf(X + searchDistance, board.columnSize - 1)).forEach { xPos ->
            (maxOf(0, Y - searchDistance)..minOf(Y + searchDistance, board.rowSize - 1))
                    .filter { yPos ->
                        !(xPos == X && yPos == Y)
                    }
                    .forEach { yPos ->
                        Timber.d("Scanning position - X:$xPos Y:$yPos")
                        cellList.add(board.getCell(Position(xPos, yPos)))
                    }
        }

        return cellList
    }

    private fun runCheckInDirection(position: Position, direction: Direction, cellType: CellType) {

    }

    private fun getRelativePositionOfDirection(direction: Direction) {
        val relativePosition = when (direction) {
            is Direction.North -> Position(0, 1)
            is Direction.East -> Position(0, 1)
            is Direction.South -> Position(0, 1)
            is Direction.West -> Position(0, 1)

        }
    }


    private fun getDirectionOfPositionFromPosition(thisPosition: Position, otherPosition: Position): Direction {
        val (thisX, thisY) = thisPosition
        val (otherX, otherY) = otherPosition
        return when {
            otherY == thisY -> when {
                otherX > thisX -> Direction.East
                else -> Direction.West
            }
            otherY < thisY -> //We're in the north
                when {
                    otherX > thisX -> Direction.NorthEast
                    otherX < thisX -> Direction.NorthWest
                    else -> Direction.North
                }
            else -> //We're in the south
                when {
                    otherX > thisX -> Direction.SouthEast
                    otherX < thisX -> Direction.SouthWest
                    else -> Direction.South
                }
        }
    }
}