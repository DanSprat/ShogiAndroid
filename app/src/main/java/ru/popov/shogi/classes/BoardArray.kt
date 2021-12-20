package ru.popov.shogi.classes

import ru.popov.shogi.classes.figures.Figure

class BoardArray {
    private var board:Array<Array<Figure?>> = Array<Array<Figure?>>(9) { Array(9) { null } }
    public operator fun get(i:Int,j:Int): Figure? {
        return board[9-j][i-1]
    }
    public operator fun set(i:Int,j:Int,figure: Figure){
        board[9-j][i-1] = figure
    }
}