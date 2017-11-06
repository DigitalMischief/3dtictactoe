package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.models.Board

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
}