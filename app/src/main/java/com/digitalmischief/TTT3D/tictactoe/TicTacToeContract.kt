package com.digitalmischief.TTT3D.tictactoe

import com.digitalmischief.TTT3D.BasePresenter
import com.digitalmischief.TTT3D.BaseView
import com.digitalmischief.TTT3D.models.Board
import com.digitalmischief.TTT3D.models.CellType

/**
 * Created by andy on 11/4/17.
 */
interface TicTacToeContract {

    interface View: BaseView<Presenter>{
        fun showBoard(board: Board)
        fun refreshBoard()
        fun setCellType(index: Int, type: String)
        fun showPlayerFault()
    }

    interface Presenter: BasePresenter, AdapterPresenter {
        fun createGameManager()
        fun getSpanSize() : Int
        fun getPlayerSymbol() : CellType
        fun resetGame()
    }

    interface AdapterPresenter {
        fun onBindCellAtPosition(position: Int, cellView: CellView)
        fun handleClickOnIndex(index: Int)
        fun getCellCount(): Int
    }

    interface CellView {
        fun displayCellType(type: String)
    }
}