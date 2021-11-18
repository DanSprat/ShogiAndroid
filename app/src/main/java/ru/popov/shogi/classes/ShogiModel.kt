package ru.popov.shogi.classes

import ru.popov.shogi.classes.figures.*

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

        // Adding pawns
        for (i in 1..9){
            Coordinate(i,3).let { figuresOnBoard.put(it,Pawn(Side.WHITE)) }
            Coordinate(i,7).let { figuresOnBoard.put(it,Pawn(Side.BLACK)) }
        }

        // Adding Bishops
        Coordinate(2,2).let { figuresOnBoard.put(it,Bishop(Side.WHITE)) }
        Coordinate(8,8).let { figuresOnBoard.put(it,Bishop(Side.BLACK)) }

        // Adding Rooks
        Coordinate(8,2).let { figuresOnBoard.put(it,Rook(Side.WHITE)) }
        Coordinate(2,8).let { figuresOnBoard.put(it,Rook(Side.BLACK)) }

        // Adding Kings
        Coordinate(5,1).let { figuresOnBoard.put(it,King(Side.WHITE)) }
        Coordinate(5,9).let { figuresOnBoard.put(it,King(Side.BLACK)) }

        // Adding Lances
        Coordinate(1,1).let { figuresOnBoard.put(it,Lance(Side.WHITE)) }
        Coordinate(9,1).let { figuresOnBoard.put(it,Lance(Side.WHITE)) }
        Coordinate(1,9).let { figuresOnBoard.put(it,Lance(Side.BLACK)) }
        Coordinate(9,9).let { figuresOnBoard.put(it,Lance(Side.BLACK)) }

        // Adding Knights
        Coordinate(2,1).let { figuresOnBoard.put(it,Knight(Side.WHITE)) }
        Coordinate(8,1).let { figuresOnBoard.put(it,Knight(Side.WHITE)) }
        Coordinate(2,9).let { figuresOnBoard.put(it,Knight(Side.BLACK)) }
        Coordinate(8,9).let { figuresOnBoard.put(it,Knight(Side.BLACK)) }

        // Adding Silvers
        Coordinate(3,1).let { figuresOnBoard.put(it,Silver(Side.WHITE)) }
        Coordinate(7,1).let { figuresOnBoard.put(it,Silver(Side.WHITE)) }
        Coordinate(3,9).let { figuresOnBoard.put(it,Silver(Side.BLACK)) }
        Coordinate(7,9).let { figuresOnBoard.put(it,Silver(Side.BLACK)) }

        // Adding Golds
        Coordinate(4,1).let { figuresOnBoard.put(it,Gold(Side.WHITE)) }
        Coordinate(6,1).let { figuresOnBoard.put(it,Gold(Side.WHITE)) }
        Coordinate(4,9).let { figuresOnBoard.put(it,Gold(Side.BLACK)) }
        Coordinate(6,9).let { figuresOnBoard.put(it,Gold(Side.BLACK)) }
        
    }
}