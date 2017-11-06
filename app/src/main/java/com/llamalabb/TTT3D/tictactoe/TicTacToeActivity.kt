package com.llamalabb.TTT3D.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import com.llamalabb.TTT3D.R
import com.llamalabb.TTT3D.models.Board
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*

class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)


    val recyclerAdapter = RecyclerAdapter(Board(0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        board_recycler_view.apply{
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)

            setOnCellClickListener(this)
        }

        presenter.onStart()

    }

    override fun showBoard(board: Board) {
        recyclerAdapter.board = board
    }

    private fun setOnCellClickListener(recyclerView: RecyclerView){

    }


}
