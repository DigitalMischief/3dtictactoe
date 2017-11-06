package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Board(var columnSize: Int = 3, var rowSize: Int = 3) {

    private lateinit var cellMatrix: Array<Array<Cell?>>

    init{
        create()
    }

    fun create(){
        cellMatrix = Array(columnSize,{ arrayOfNulls<Cell>(rowSize)})
        for(i in 0 until columnSize -1){
            var boardRow = Array<Cell?>(rowSize, {null})
            for(j in 0 until boardRow.size){
                boardRow[j] = Cell(Position(i, j))
            }

            cellMatrix[i] = boardRow
        }
    }

    fun getCell(position: Position) = cellMatrix[position.X][position.Y]

    fun getCellCount() = columnSize*rowSize

}