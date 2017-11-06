package com.llamalabb.TTT3D.tictactoe

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.llamalabb.TTT3D.GameManager
import com.llamalabb.TTT3D.R
import com.llamalabb.TTT3D.models.Position

class TicTacToeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tic_tac_toe)

        setupGame()
    }

    fun setupGame() {
        val manager = GameManager()
        manager.scanNeighbors(Position(4,5))
    }


}
