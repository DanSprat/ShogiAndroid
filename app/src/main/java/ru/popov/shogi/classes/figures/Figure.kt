package ru.popov.shogi.classes.figures

import ru.popov.shogi.classes.ShogiRules

abstract class Figure {

    protected abstract var rules: ShogiRules
    protected abstract var side: Side
    protected abstract val promotable:Boolean

}

class Pawn(override var side:Side):Figure() {

    companion object {
        val name: String
            get() = "Pawn"
        val promotedName: String
            get() = "Tokin"

    }

    override val promotable: Boolean = true
    override var rules: ShogiRules = if(side == Side.BLACK){
        ShogiRules.PAWN_BLACK
    } else {
        ShogiRules.PAWN_WHITE
    }


}