package com.llamalabb.TTT3D.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.llamalabb.TTT3D.R
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.util.Utils
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.cell_layout.view.*

class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)

    private val boardListener = object : TicTacToeContract.BoardListener {
        override fun cellClickListener(index: Int) {
            presenter.handleClickOnIndex(index)
        }

        override fun cellLongClickListener(index: Int) {

        }

        override fun cellBinding(index: Int, cellView: TicTacToeContract.CellView){
            presenter.onBindCellAtPosition(index, cellView)
        }

        override fun getCellCount() : Int = presenter.getCellCount()

    }


    private val recyclerAdapter = BoardRecyclerAdapter(boardListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)


        board_recycler_view.apply{
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        presenter.onStart()

    }

    override fun showBoard(board: Board) {
        board_recycler_view.layoutManager =
                GridLayoutManager(this, presenter.getSpanSize())
        refreshBoard()
    }

    override fun refreshBoard() {
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun setCellType( index: Int, type: String ){
        board_recycler_view.layoutManager.findViewByPosition(index).cell_type_text.text = type
    }

    override fun showPlayerFault(){
        Utils.showMessageShort(this,"watch doin checker?")
    }
}