package com.llamalabb.TTT3D.tictactoe

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.llamalabb.TTT3D.R
import com.llamalabb.TTT3D.models.Board

/**
 * Created by andy on 11/5/17.
 */
class RecyclerAdapter(board: Board) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    var board = board
        set(board){
            field = board
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
         var cellType: TextView = view.findViewById(R.id.cell_type_text)
    }
    override fun getItemCount(): Int {
        return board.getCellCount()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_layout, parent, false)

        return ViewHolder(view)
    }
}