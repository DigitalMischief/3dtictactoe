package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Board(val boardSize: Int) {

    lateinit var board: Array<Array<Cell?>>


    fun createBoard(){
        for(i in 0..boardSize -1){
            var boardRow = Array<Cell?>(boardSize, {null})
            for(j in 0..boardRow.size -1){
                boardRow[j] = Cell(Position(i, j))
            }



            board[i] = boardRow
        }
    }
}