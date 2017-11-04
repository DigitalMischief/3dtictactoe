package com.llamalabb.TTT3D.models

/**
 * Created by brandon on 11/4/17.
 */
sealed class Direction {
    open class Horizontal()
    open class Vertical()
    open class Diagnol()
    open class Adiagnol()


    object North: Vertical()
    object NorthEast: Diagnol()
    object NorthWest: Adiagnol()
    object South: Vertical()
    object SouthEast: Adiagnol()
    object SouthWest: Diagnol()
    object East: Horizontal()
    object West: Horizontal()
}