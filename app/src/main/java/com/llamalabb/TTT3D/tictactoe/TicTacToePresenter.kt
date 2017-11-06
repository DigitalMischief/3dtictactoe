package com.llamalabb.TTT3D.tictactoe

import com.llamalabb.TTT3D.GameManager
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.models.CellType
import com.llamalabb.TTT3D.models.Position

/**
 * Created by andy on 11/4/17.
 */

class TicTacToePresenter(var view: TicTacToeContract.View) : TicTacToeContract.Presenter, TicTacToeContract.AdapterPresenter {

    lateinit var gameManager: GameManager
    lateinit var board: Board

    private var turnCount = 0

    override fun onStart() {
        board = Board(3,3)
        createGameManager()
        view.showBoard(board)
    }

    override fun createGameManager() {
        gameManager = GameManager(board)
    }

    override fun getSpanSize() : Int = board.rowSize

    override fun getPlayerSymbol(): CellType {
        turnCount.inc()
        return if (turnCount % 2 == 0) CellType.X else CellType.O

    }

    override fun onBindCellAtPosition(position: Int, cellView: TicTacToeContract.CellView) {
        with(board) {
            convertIndexToPosition(position).run(this::getCell).let {
                cellView.displayCellType(it.getTypeAsString()) //setup whatever view things needed
            }
        }
    }

    override fun getCellCount(): Int = board.getCellCount()

    override fun handleClickOnIndex(index: Int) {
        val position = board.convertIndexToPosition(index)

         board.getCell(position).let{
             if (it.isAvailable()){
                 it.type = getPlayerSymbol()
                 view.refreshBoard()
             } else {
                 view.showPlayerFault()
             }
         }
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