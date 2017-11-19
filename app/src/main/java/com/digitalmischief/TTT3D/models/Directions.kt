package com.digitalmischief.TTT3D.models

/**
 * Created by brandon on 11/4/17.
 */
sealed class Direction {
    open class Horizontal: Direction()
    open class Vertical: Direction()
    open class Diagnol: Direction()
    open class Adiagnol: Direction()


    object North: Vertical()
    object NorthEast: Diagnol()
    object NorthWest: Adiagnol()
    object South: Vertical()
    object SouthEast: Adiagnol()
    object SouthWest: Diagnol()
    object East: Horizontal()
    object West: Horizontal()
}