package com.llamalabb.TTT3D.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.llamalabb.TTT3D.R
import com.llamalabb.TTT3D.CellClickListener
import com.llamalabb.TTT3D.models.Board
import com.llamalabb.TTT3D.util.Utils
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.cell_layout.view.*

class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)


    private val recyclerAdapter = RecyclerAdapter(Board(0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)


        board_recycler_view.apply{
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
            setCellClickListener(this)
        }

        presenter.onStart()

    }

    override fun showBoard(board: Board) {
        board_recycler_view.layoutManager =
                GridLayoutManager(this, presenter.getSpanSize())
        recyclerAdapter.board = board
    }

    override fun setCellType( index: Int, type: String ){
        board_recycler_view.layoutManager.findViewByPosition(index).cell_type_text.text = type
    }

    override fun showPlayerFault(){
        Utils.showMessageShort("Naw yo, watch doin checker?")
    }

    private fun setCellClickListener(view: RecyclerView){
        val itemClickListener = object : CellClickListener.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                presenter.handleClickOnIndex(position)
            }
            override fun onLongItemClick(view: View, position: Int) {

            }
        }

        view.addOnItemTouchListener(CellClickListener(this, view, itemClickListener))
    }



}