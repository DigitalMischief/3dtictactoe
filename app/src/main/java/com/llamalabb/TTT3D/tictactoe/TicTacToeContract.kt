package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.BasePresenter
import com.llamalabb.TTT3D.BaseView
import com.llamalabb.TTT3D.models.Board

/**
 * Created by andy on 11/4/17.
 */
interface TicTacToeContract {

    interface View: BaseView<Presenter>{
        fun showBoard(board: Board)
    }

    interface Presenter: BasePresenter{
        fun createBoard()
    }
}