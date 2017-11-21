package com.digitalmischief.TTT3D.tictactoe

import com.digitalmischief.TTT3D.GameManager
import com.digitalmischief.TTT3D.models.Board
import com.digitalmischief.TTT3D.models.CellType
import com.digitalmischief.TTT3D.models.Position
import java.io.*


/**
 * Created by andy on 11/4/17.
 */

class TicTacToePresenter(var view: TicTacToeContract.View)
    : TicTacToeContract.Presenter,
        TicTacToeContract.AdapterPresenter {

    lateinit var gameManager: GameManager
    lateinit var board: Board

    var turnCount = 0

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
        turnCount+=1
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

        if(view.isMyTurn()) {
            board.getCell(position).let {
                if (it.isAvailable()) {
                    it.type = getPlayerSymbol()
                    view.refreshBoard()
                } else {
                    view.showPlayerFault()
                }
            }
            view.takeTurn()
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

    override fun serializeBoard(): ByteArray?{
        val arrOutStream = ByteArrayOutputStream()
        try {
            val out = ObjectOutputStream(arrOutStream)
            out.writeObject(board)
            return arrOutStream.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null

    }

    override fun deserializeBoard(byteArray: ByteArray){
        val arrInStream = ByteArrayInputStream(byteArray)
        try {
            val objectInStream = ObjectInputStream(arrInStream)
            board = objectInStream.readObject() as Board
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}