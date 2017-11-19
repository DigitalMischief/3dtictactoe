package com.digitalmischief.TTT3D.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.digitalmischief.TTT3D.R
import com.digitalmischief.TTT3D.models.Board
import com.digitalmischief.TTT3D.util.Utils
import kotlinx.android.synthetic.main.activity_tic_tac_toe.*
import kotlinx.android.synthetic.main.cell_layout.view.*
import timber.log.Timber

class TicTacToeActivity : AppCompatActivity(), TicTacToeContract.View {
    override var presenter: TicTacToeContract.Presenter = TicTacToePresenter(this)
    val RESET_ID = 1


    private val recyclerAdapter = BoardRecyclerAdapter(presenter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)
        setSupportActionBar(toolbar)
        board_recycler_view.apply{
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, 3)
        }

        presenter.onStart()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //reset button
        menu.add(0,1,RESET_ID, "Reset").apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_WITH_TEXT)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("$item")
        when (item.itemId) {
            RESET_ID -> {
                presenter.resetGame()
            }
        }
        return super.onOptionsItemSelected(item)
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