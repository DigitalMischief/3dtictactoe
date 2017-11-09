package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Board(var columnSize: Int = 3, var rowSize: Int = 3) {

    private lateinit var cellMatrix: ArrayList<ArrayList<Cell>>

    init{
        create()
    }

    private fun create(){
        cellMatrix = ArrayList()

        for(i in 0 until columnSize){
            val boardRow = ArrayList<Cell>()
            (0 until rowSize).mapTo(boardRow) {
                Cell( position = Position(i, it))
            }
            cellMatrix.add(boardRow)
        }
    }

    fun getCell(position: Position) : Cell {

        try {
            return cellMatrix[position.Y][position.X]
        } catch(e: ArrayIndexOutOfBoundsException){
            throw Exception("Off the board")
        }
    }

    fun convertIndexToPosition(index: Int) : Position{

        val span = rowSize
        val col = index % span
        val row = Math.floor(index.toDouble()/span.toDouble()).toInt()

        return Position( col, row)

    }

    fun getCellCount() = columnSize*rowSize

}



