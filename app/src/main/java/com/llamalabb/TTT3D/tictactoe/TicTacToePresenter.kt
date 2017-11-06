package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.GameManager
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.Position

/**
 * Created by andy on 11/4/17.
 */

class TicTacToePresenter(var view: TicTacToeContract.View) : TicTacToeContract.Presenter{
    override fun onStart() {
        createBoard()

        view.showBoard(Board())
    }

    override fun createBoard() {


    }

    private fun runTest() {
        val testBoard = Board()
        val manager = GameManager(testBoard)
        val position = Position(1, 1)
        val ourCell = testBoard.getCell(position)
        val ourType = ourCell.type
        ourType?.let {
            manager.getAlliesInSearchArea(position, ourType)
        }
    }
}