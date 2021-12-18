package ru.popov.shogi.classes.figures

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import ru.popov.shogi.R
import ru.popov.shogi.classes.PieceView
import ru.popov.shogi.classes.ShogiRules

abstract class Figure {

    companion object {
        var draws: HashMap<FigureName,Int> = HashMap()
        init {
            draws[FigureName.ROOK] = R.drawable.rook
            draws[FigureName.PAWN] = R.drawable.pawn
            draws[FigureName.KNIGHT] = R.drawable.knight
            draws[FigureName.KING] = R.drawable.king
            draws[FigureName.GOLD] = R.drawable.gold
            draws[FigureName.BISHOP] = R.drawable.bishop
            draws[FigureName.SILVER] = R.drawable.silver
            draws[FigureName.LANCE] = R.drawable.lance

            draws[FigureName.TOKIN] = R.drawable.tokin
            draws[FigureName.PROMOTED_BISHOP] = R.drawable.promoted_bishop
            draws[FigureName.PROMOTED_LANCE] = R.drawable.promoted_lance
            draws[FigureName.PROMOTED_SILVER] = R.drawable.promoted_silver
            draws[FigureName.PROMOTED_ROOK] = R.drawable.promoted_rook
            draws[FigureName.PROMOTED_KNIGHT] = R.drawable.promoted_knight
        }
    }
    abstract var row:Int
    abstract var col:Int
    abstract var pieceImage: PieceView
    protected abstract var name: FigureName
    protected abstract var abbrName: String
    protected abstract var rules: ShogiRules
    protected abstract var side: Side
    protected abstract val promotable: Boolean
    protected abstract val promoted: Boolean


    protected abstract fun reset()
    protected abstract fun promote(): Boolean

    fun changeSide() {
        reset()
        rules = rules.next()
        side = side.next()
    }

}

class Pawn(override var side: Side, override var row: Int, override var col: Int,
           override val promoted: Boolean,Rid:Int,context: Context,noteSize:Int,x:Float,y:Float,layout: RelativeLayout,
            layoutParams:ViewGroup.LayoutParams) : Figure() {
    override lateinit var pieceImage: PieceView
    override lateinit var abbrName: String
    override lateinit var name: FigureName
    override lateinit var rules: ShogiRules

    init {
        var pieceView = PieceView(context,this)
        pieceView.setImageResource(Rid)

        pieceView.layoutParams = layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.TOKIN
        if(promoted){
            this.name = promotedName
            abbrName = abbrPromotedName
            rules = if (side == Side.WHITE){
                ShogiRules.GOLD_WHITE
            } else {
                ShogiRules.GOLD_BLACK
            }
        } else {
            this.name = Pawn.name
            abbrName = Pawn.abbrName
            rules = if (side == Side.WHITE){
                ShogiRules.PAWN_WHITE
            } else {
                ShogiRules.PAWN_BLACK
            }
        }
        pieceImage = pieceView
        layout.addView(pieceView)
    }

    companion object {
        val name: FigureName
            get() = FigureName.PAWN
        val promotedName: FigureName
            get() = FigureName.TOKIN
        val abbrName: String
            get() = "P"
        val abbrPromotedName: String
            get() = "T"

    }

    override val promotable: Boolean = true

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

//class Silver(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Silver"
//        val promotedName: String
//            get() = "Promoted Silver"
//        val abbrName: String
//            get() = "S"
//        val abbrPromotedName: String
//            get() = "PS"
//
//   }
//
//    override var name: String = Silver.name
//    override var abbrName: String = Silver.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.SILVER_WHITE
//    } else {
//        ShogiRules.SILVER_BLACK
//    }
//
//    override fun promote(): Boolean {
//        name = promotedName
//        abbrName = abbrPromotedName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.GOLD_WHITE
//        } else {
//            ShogiRules.GOLD_BLACK
//        }
//        return true
//    }
//
//    override fun reset() {
//        name = Silver.name
//        abbrName = Silver.abbrName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.SILVER_WHITE
//        } else {
//            ShogiRules.SILVER_BLACK
//        }
//    }
//
//}
//
//class Gold(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Gold"
//        val abbrName: String
//            get() = "G"
//
//    }
//
//    override var name: String = Silver.name
//    override var abbrName: String = Silver.abbrName
//    override val promotable: Boolean = false
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.SILVER_WHITE
//    } else {
//        ShogiRules.SILVER_BLACK
//    }
//
//    override fun promote(): Boolean {
//        return false
//    }
//
//    override fun reset() {}
//
//}
//
//class Lance(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Lance"
//        val promotedName: String
//            get() = "Promoted Lance"
//        val abbrName: String
//            get() = "L"
//        val abbrPromotedName: String
//            get() = "PL"
//
//    }
//
//    override var name: String = Lance.name
//    override var abbrName: String = Lance.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.LANCE_WHITE
//    } else {
//        ShogiRules.LANCE_BLACK
//    }
//
//    override fun promote(): Boolean {
//        name = promotedName
//        abbrName = abbrPromotedName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.GOLD_WHITE
//        } else {
//            ShogiRules.GOLD_BLACK
//        }
//        return true
//    }
//
//    override fun reset() {
//        name = Lance.name
//        abbrName = Lance.abbrName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.LANCE_WHITE
//        } else {
//            ShogiRules.LANCE_BLACK
//        }
//    }
//
//}
//
//class Knight(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Knight"
//        val promotedName: String
//            get() = "Promoted Knight"
//        val abbrName: String
//            get() = "N"
//        val abbrPromotedName: String
//            get() = "PN"
//
//    }
//
//    override var name: String = Knight.name
//    override var abbrName: String = Knight.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.KNIGHT_WHITE
//    } else {
//        ShogiRules.KNIGHT_BLACK
//    }
//
//    override fun promote(): Boolean {
//        name = promotedName
//        abbrName = abbrPromotedName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.GOLD_WHITE
//        } else {
//            ShogiRules.GOLD_BLACK
//        }
//        return true
//    }
//
//    override fun reset() {
//        name = Knight.name
//        abbrName = Knight.abbrName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.KNIGHT_WHITE
//        } else {
//            ShogiRules.KNIGHT_BLACK
//        }
//    }
//
//}
//
//class Rook(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Rook"
//        val promotedName: String
//            get() = "Dragon"
//        val abbrName: String
//            get() = "R"
//        val abbrPromotedName: String
//            get() = "+R"
//
//    }
//
//    override var name: String = Rook.name
//    override var abbrName: String = Rook.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.ROOK
//    } else {
//        ShogiRules.ROOK
//    }
//
//    override fun promote(): Boolean {
//        name = promotedName
//        abbrName = abbrPromotedName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.DRAGON
//        } else {
//            ShogiRules.DRAGON
//        }
//        return true
//    }
//
//    override fun reset() {
//        name = Rook.name
//        abbrName = Rook.abbrName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.ROOK
//        } else {
//            ShogiRules.ROOK
//        }
//    }
//
//}
//
//class Bishop(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "Bishop"
//        val promotedName: String
//            get() = "Horse"
//        val abbrName: String
//            get() = "B"
//        val abbrPromotedName: String
//            get() = "+B"
//
//    }
//
//    override var name: String = Bishop.name
//    override var abbrName: String = Bishop.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = if (side == Side.WHITE) {
//        ShogiRules.BISHOP
//    } else {
//        ShogiRules.BISHOP
//    }
//
//    override fun promote(): Boolean {
//        name = promotedName
//        abbrName = abbrPromotedName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.HORSE
//        } else {
//            ShogiRules.HORSE
//        }
//        return true
//    }
//
//    override fun reset() {
//        name = Silver.name
//        abbrName = Silver.abbrName
//        rules = if (side == Side.WHITE) {
//            ShogiRules.BISHOP
//        } else {
//            ShogiRules.BISHOP
//        }
//    }
//
//}
//
//class King(override var side: Side) : Figure() {
//
//    companion object {
//        val name: String
//            get() = "King"
//        val abbrName: String
//            get() = "K"
//
//    }
//
//    override var name: String = King.name
//    override var abbrName: String = King.abbrName
//    override val promotable: Boolean = true
//    override var rules: ShogiRules = ShogiRules.KING
//
//    override fun promote(): Boolean {
//        return false
//    }
//
//    override fun reset() {}
//}
