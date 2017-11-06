package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.GameManager
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.CellType
import com.llamalabb.TTT3D.models.Position

/**
 * Created by andy on 11/4/17.
 */

class TicTacToePresenter(var view: TicTacToeContract.View) : TicTacToeContract.Presenter{

    lateinit var gameManager: GameManager
    lateinit var board: Board

    override fun onStart() {
        board = Board()
        createGameManager()
        view.showBoard(board)
    }

    override fun createGameManager() {
        gameManager = GameManager(board)
    }

    override fun getSpanSize() : Int = board.rowSize

    override fun getPlayerSymbol(): String {
        return "" //TODO: player logic not yet implemented
    }

    override fun handleClickOnIndex(index: Int) {

        val position = convertIndexToPosition(index)

         board.getCell(position)?.let{
             if (it.isAvailable()){
                 it.type = CellType.X
                 view.setCellType(index, it.type.toString())
             } else {
                 view.showPlayerFault()
             }
         }
    }

    private fun convertIndexToPosition(index: Int) : Position{

        val pos = index + 1
        val span = board.rowSize
        val col = pos % span
        val row = Math.floor((pos.toDouble())/(span.toDouble())).toInt()

        return when (col) {
            0 -> Position(span, row)
            else -> Position( col - 1, row)
        }

    }

}