package com.digitalmischief.TTT3D.tictactoe

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.digitalmischief.TTT3D.R
import com.digitalmischief.TTT3D.models.Board
import com.digitalmischief.TTT3D.util.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.cell_layout.view.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult



class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)

    private val RC_SIGN_IN = 9000


    private val recyclerAdapter = BoardRecyclerAdapter(presenter)

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