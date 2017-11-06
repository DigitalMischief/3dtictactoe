package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Board(var columnSize: Int = 3, var rowSize: Int = 3) {

    private lateinit var cellMatrix: ArrayList<ArrayList<Cell>>

    init{
        create()
    }

    fun create(){
        cellMatrix = ArrayList()
        for(i in 0 until columnSize - 1){
            val boardRow = ArrayList<Cell>()
            for(j in 0 until rowSize - 1){
                boardRow.add(Cell(Position(i, j)))
            }
            cellMatrix.add(boardRow)
        }
    }

    fun getCell(position: Position) = cellMatrix[position.Y][position.X]

    fun getCellCount() = columnSize*rowSize

}