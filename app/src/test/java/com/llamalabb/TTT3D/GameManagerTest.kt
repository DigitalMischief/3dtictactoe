package com.llamalabb.TTT3D

import com.digitalmischief.TTT3D.models.Board
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by brandon on 11/8/17.
 */
class GameManagerTest {
    @Before
    fun setUp() {
    }

    @Test
    fun `Board cell count initializes with 9`() {
        //given
        val board = Board()
        //when
        val result = board.getCellCount()
        //then
        Assert.assertEquals(9, result)
    }

}