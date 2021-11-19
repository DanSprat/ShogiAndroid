package ru.popov.shogi.classes.figures

import ru.popov.shogi.classes.ShogiRules

abstract class Figure {

    protected abstract var name: String
    protected abstract var abbrName: String
    protected abstract var rules: ShogiRules
    protected abstract var side: Side
    protected abstract val promotable: Boolean

    protected abstract fun reset()
    protected abstract fun promote(): Boolean
    fun changeSide() {
        reset()
        rules = rules.next()
        side = side.next()
    }

}

class Pawn(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Pawn"
        val promotedName: String
            get() = "Tokin"
        val abbrName: String
            get() = "P"
        val abbrPromotedName: String
            get() = "T"

    }

    override var abbrName: String = Pawn.abbrName
    override var name = Pawn.name
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.BLACK) {
        ShogiRules.PAWN_BLACK
    } else {
        ShogiRules.PAWN_WHITE
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.GOLD_WHITE
        } else {
            ShogiRules.GOLD_BLACK
        }
        return true
    }

    override fun reset() {
        name = Pawn.name
        abbrName = Pawn.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.PAWN_WHITE
        } else {
            ShogiRules.PAWN_BLACK
        }
    }

}

class Silver(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Silver"
        val promotedName: String
            get() = "Promoted Silver"
        val abbrName: String
            get() = "S"
        val abbrPromotedName: String
            get() = "PS"

    }

    override var name: String = Silver.name
    override var abbrName: String = Silver.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.SILVER_WHITE
    } else {
        ShogiRules.SILVER_BLACK
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.GOLD_WHITE
        } else {
            ShogiRules.GOLD_BLACK
        }
        return true
    }

    override fun reset() {
        name = Silver.name
        abbrName = Silver.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.SILVER_WHITE
        } else {
            ShogiRules.SILVER_BLACK
        }
    }

}

class Gold(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Gold"
        val abbrName: String
            get() = "G"

    }

    override var name: String = Silver.name
    override var abbrName: String = Silver.abbrName
    override val promotable: Boolean = false
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.SILVER_WHITE
    } else {
        ShogiRules.SILVER_BLACK
    }

    override fun promote(): Boolean {
        return false
    }

    override fun reset() {}

}

class Lance(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Lance"
        val promotedName: String
            get() = "Promoted Lance"
        val abbrName: String
            get() = "L"
        val abbrPromotedName: String
            get() = "PL"

    }

    override var name: String = Lance.name
    override var abbrName: String = Lance.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.LANCE_WHITE
    } else {
        ShogiRules.LANCE_BLACK
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.GOLD_WHITE
        } else {
            ShogiRules.GOLD_BLACK
        }
        return true
    }

    override fun reset() {
        name = Lance.name
        abbrName = Lance.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.LANCE_WHITE
        } else {
            ShogiRules.LANCE_BLACK
        }
    }

}

class Knight(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Knight"
        val promotedName: String
            get() = "Promoted Knight"
        val abbrName: String
            get() = "N"
        val abbrPromotedName: String
            get() = "PN"

    }

    override var name: String = Knight.name
    override var abbrName: String = Knight.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.KNIGHT_WHITE
    } else {
        ShogiRules.KNIGHT_BLACK
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.GOLD_WHITE
        } else {
            ShogiRules.GOLD_BLACK
        }
        return true
    }

    override fun reset() {
        name = Knight.name
        abbrName = Knight.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.KNIGHT_WHITE
        } else {
            ShogiRules.KNIGHT_BLACK
        }
    }

}

class Rook(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Rook"
        val promotedName: String
            get() = "Dragon"
        val abbrName: String
            get() = "R"
        val abbrPromotedName: String
            get() = "+R"

    }

    override var name: String = Rook.name
    override var abbrName: String = Rook.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.ROOK
    } else {
        ShogiRules.ROOK
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.DRAGON
        } else {
            ShogiRules.DRAGON
        }
        return true
    }

    override fun reset() {
        name = Rook.name
        abbrName = Rook.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.ROOK
        } else {
            ShogiRules.ROOK
        }
    }

}

class Bishop(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "Bishop"
        val promotedName: String
            get() = "Horse"
        val abbrName: String
            get() = "B"
        val abbrPromotedName: String
            get() = "+B"

    }

    override var name: String = Bishop.name
    override var abbrName: String = Bishop.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.BISHOP
    } else {
        ShogiRules.BISHOP
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        rules = if (side == Side.WHITE) {
            ShogiRules.HORSE
        } else {
            ShogiRules.HORSE
        }
        return true
    }

    override fun reset() {
        name = Silver.name
        abbrName = Silver.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.BISHOP
        } else {
            ShogiRules.BISHOP
        }
    }

}

class King(override var side: Side) : Figure() {

    companion object {
        val name: String
            get() = "King"
        val abbrName: String
            get() = "K"

    }

    override var name: String = King.name
    override var abbrName: String = King.abbrName
    override val promotable: Boolean = true
    override var rules: ShogiRules = ShogiRules.KING

    override fun promote(): Boolean {
        return false
    }

    override fun reset() {}
}