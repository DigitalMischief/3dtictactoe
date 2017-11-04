package com.llamalabb.TTT3D.models

/**
 * Created by andy on 11/4/17.
 */
data class Board(val boardSize: Int) {

   var board = Array(boardSize, {Array(boardSize, {Cell()})})


    fun createBoard(){
        for(i in 0..board.size -1){
            var boardRow = Array(boardSize, {Cell()})
            for(j in 0..boardRow.size -1){
                boardRow[j] = Cell()
            }
            board[i] = boardRow
        }
    }
}