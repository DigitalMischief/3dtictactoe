package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.BasePresenter
import com.llamalabb.TTT3D.BaseView
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.Position

/**
 * Created by andy on 11/4/17.
 */
interface TicTacToeContract {

    interface View: BaseView<Presenter>{
        fun showBoard(board: Board)
        fun setCellType(index: Int, type: String)
        fun showPlayerFault()
    }

    interface Presenter: BasePresenter{
        fun createGameManager()
        fun getSpanSize() : Int
        fun getPlayerSymbol() : String
        fun handleClickOnIndex(index: Int)
    }
}