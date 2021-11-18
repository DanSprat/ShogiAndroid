package ru.popov.shogi.classes

import ru.popov.shogi.classes.figures.Figure

class ShogiModel {
    private val figuresOnBoard:MutableMap<Coordinate,Figure> = mutableMapOf()

    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()

    init {
        reset()
    }

    private fun reset(){
        figuresOnBoard.clear();
        handBlack.clear()
        handWhite.clear()

        figuresOnBoard.put()

    }
}