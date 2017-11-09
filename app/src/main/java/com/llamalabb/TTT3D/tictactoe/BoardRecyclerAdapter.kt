package com.llamalabb.TTT3D.tictactoe

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.llamalabb.TTT3D.R
import timber.log.Timber

/**
 * Created by andy on 11/5/17.
 */
class BoardRecyclerAdapter(private val presenter: TicTacToeContract.AdapterPresenter)
    : RecyclerView.Adapter<BoardRecyclerAdapter.CellViewHolder>() {

    override fun getItemCount(): Int {
        return presenter.getCellCount()
    }

    override fun onBindViewHolder(holder: CellViewHolder, position: Int) {
        presenter.onBindCellAtPosition(position, holder)
        holder.cell.setOnClickListener {
            Timber.d("Cell clicked at adapter position $position")
            presenter.handleClickOnIndex(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_layout, parent, false)

        return CellViewHolder(view)
    }

    class CellViewHolder(view: View) : RecyclerView.ViewHolder(view), TicTacToeContract.CellView{
        private var cellTypeText: TextView = view.findViewById(R.id.cell_type_text)
        var cell: CardView = view.findViewById(R.id.cell_card)
        override fun displayCellType(type: String) {
            cellTypeText.text = type
        }
    }
}