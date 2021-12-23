package ru.popov.shogi.classes.figures

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import ru.popov.shogi.R
import ru.popov.shogi.classes.AppInfo
import ru.popov.shogi.classes.PieceView
import ru.popov.shogi.classes.ShogiRules

abstract class Figure {

    companion object {
        var draws: HashMap<FigureName,Int> = HashMap()
    }

    protected abstract var listener:View.OnClickListener
    abstract var row:Int
    abstract var col:Int
    abstract var pieceImage: PieceView
    protected abstract var name: FigureName
    protected abstract var abbrName: String
    abstract var rules: ShogiRules
    abstract var side: Side
    protected abstract val promotable: Boolean
    protected abstract var promoted: Boolean
    protected abstract val appInfo:AppInfo
    protected abstract val orientation:Orientation

    protected abstract fun changeImages()
    protected abstract fun reset()
    protected abstract fun promote(): Boolean

    fun changeSide() {
        reset()
        rules = rules.next()
        side = side.next()
    }

}

class Pawn(override var side: Side, override var row: Int, override var col: Int,
           override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
           override val orientation: Orientation, override var listener: View.OnClickListener) : Figure() {

    override lateinit var pieceImage: PieceView
    override lateinit var abbrName: String
    override lateinit var name: FigureName
    override lateinit var rules: ShogiRules
    private var commonID:Int = 0
    private var promotedID:Int = 0

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.pawn_0
                promotedID = R.drawable.tokin_0
            } else {
                commonID = R.drawable.pawn_1
                promotedID = R.drawable.tokin_1
            }
        } else {
            if (side == Side.BLACK) {
                commonID = R.drawable.pawn_0
                promotedID = R.drawable.tokin_0
            } else {
                commonID = R.drawable.pawn_1
                promotedID = R.drawable.tokin_1
            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
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
        appInfo.layout.addView(pieceView)
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

        private var commonId_0 =  R.drawable.pawn_0
        private var promotedId_0:Int = R.drawable.tokin_0

        private var commonId_1 =  R.drawable.pawn_1
        private var promotedId_1:Int = R.drawable.tokin_1


    }

    override val promotable: Boolean = true

    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE){
                commonID = commonId_1
                promotedID = promotedId_1
            } else {
                commonID = commonId_0
                promotedID = promotedId_0
            }
        } else {
            if (side == Side.BLACK){
                commonID = commonId_1
                promotedID = promotedId_1
            } else {
                commonID = commonId_0
                promotedID = promotedId_0
            }
        }
        pieceImage.setImageResource(commonID)
    }

    override fun promote(): Boolean {
        name = promotedName
        abbrName = abbrPromotedName
        pieceImage.setImageResource(promotedID)
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

class Silver(override var side: Side, override var row: Int, override var col: Int,
             override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
             override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {

    companion object {
        val name: FigureName
            get() = FigureName.SILVER
        val promotedName: FigureName
            get() = FigureName.PROMOTED_SILVER
        val abbrName: String
            get() = "S"
        val abbrPromotedName: String
            get() = "PS"

        private var commonId_0 =  R.drawable.silver_0
        private var promotedId_0:Int = R.drawable.p_silver_0

        private var commonId_1 =  R.drawable.silver_1
        private var promotedId_1:Int = R.drawable.p_silver_1

    }


    override lateinit var pieceImage: PieceView
    private var commonID:Int = 0
    private var promotedID:Int = 0
    override var name: FigureName = Silver.name
    override var abbrName: String = Silver.abbrName
    override val promotable: Boolean = true
    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE){
                commonID = Silver.commonId_1
                promotedID = Silver.promotedId_1
            } else {
                commonID = Silver.commonId_0
                promotedID = Silver.promotedId_0
            }
        } else {
            if (side == Side.BLACK){
                commonID = Silver.commonId_1
                promotedID = Silver.promotedId_1
            } else {
                commonID = Silver.commonId_0
                promotedID = Silver.promotedId_0
            }
        }
        pieceImage.setImageResource(commonID)

    }

    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.SILVER_WHITE
    } else {
        ShogiRules.SILVER_BLACK
    }

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.silver_0
                promotedID = R.drawable.p_silver_0
            } else {
                commonID = R.drawable.silver_1
                promotedID = R.drawable.p_silver_1
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.silver_0
                promotedID = R.drawable.p_silver_0
            } else {
                commonID = R.drawable.silver_1
                promotedID = R.drawable.p_silver_1
            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.TOKIN
        if(promoted){
            this.name = Pawn.promotedName
            abbrName = Pawn.abbrPromotedName
            rules = if (side == Side.WHITE){
                ShogiRules.GOLD_WHITE
            } else {
                ShogiRules.GOLD_BLACK
            }
        } else {
            this.name = Pawn.name
            abbrName = Pawn.abbrName
            rules = if (side == Side.WHITE){
                ShogiRules.SILVER_WHITE
            } else {
                ShogiRules.SILVER_BLACK
            }
        }
        pieceImage = pieceView
        appInfo.layout.addView(pieceView)
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

class Gold(override var side: Side, override var row: Int, override var col: Int,
           override val appInfo: AppInfo, x:Float, y:Float,
           override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {



    override var promoted: Boolean = false
    private var commonID:Int = 0
    override lateinit var pieceImage: PieceView
    override var name: FigureName = Gold.name
    override var abbrName: String = Gold.abbrName
    override val promotable: Boolean = false
    override fun changeImages() {
        commonID = if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                R.drawable.gold_1
            } else {
                R.drawable.gold_0
            }
        } else {
            if (side == Side.WHITE) {
                R.drawable.gold_1
            } else {
                R.drawable.gold_0

            }
        }
    }

    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.SILVER_WHITE
    } else {
        ShogiRules.SILVER_BLACK
    }

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        commonID = if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                R.drawable.gold_0
            } else {
                R.drawable.gold_1
            }
        } else {
            if (side == Side.WHITE) {
                R.drawable.gold_0
            } else {
                R.drawable.gold_1

            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.TOKIN

        this.name = Gold.name
        abbrName = Gold.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.GOLD_WHITE
        } else {
            ShogiRules.GOLD_BLACK
        }
        pieceImage = pieceView
        appInfo.layout.addView(pieceView)
    }
    companion object {
        val name: FigureName
            get() = FigureName.GOLD
        val abbrName: String
            get() = "G"

    }


    override fun promote(): Boolean {
        return false
    }

    override fun reset() {}

}

class Lance(override var side: Side, override var row: Int, override var col: Int,
            override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
            override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {

    private var commonID:Int = 0
    private var promotedID:Int = 0
    override  lateinit  var pieceImage: PieceView
    override var name: FigureName = Lance.name
    override var abbrName: String = Lance.abbrName
    override val promotable: Boolean = true

    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.lance_1
                promotedID = R.drawable.p_lance_1
            } else {
                commonID = R.drawable.lance_0
                promotedID = R.drawable.p_lance_0
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.lance_1
                promotedID = R.drawable.p_lance_1
            } else {
                commonID = R.drawable.lance_0
                promotedID = R.drawable.p_lance_0
            }
        }
    }

    override var rules: ShogiRules = if (side == Side.WHITE) {
        ShogiRules.LANCE_WHITE
    } else {
        ShogiRules.LANCE_BLACK
    }


    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.lance_0
                promotedID = R.drawable.p_lance_0
            } else {
                commonID = R.drawable.lance_1
                promotedID = R.drawable.p_lance_1
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.lance_0
                promotedID = R.drawable.p_lance_0
            } else {
                commonID = R.drawable.lance_1
                promotedID = R.drawable.p_lance_1
            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.TOKIN
        if(promoted){
            this.name = Pawn.promotedName
            abbrName = Pawn.abbrPromotedName
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
        appInfo.layout.addView(pieceView)
    }

    companion object {
        val name: FigureName
            get() = FigureName.LANCE
        val promotedName: FigureName
            get() = FigureName.PROMOTED_LANCE
        val abbrName: String
            get() = "L"
        val abbrPromotedName: String
            get() = "PL"

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

class Knight(override var side: Side, override var row: Int, override var col: Int,
             override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
             override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {

    companion object {
        val name: FigureName
            get() = FigureName.KNIGHT
        val promotedName: FigureName
            get() = FigureName.KNIGHT
        val abbrName: String
            get() = "N"
        val abbrPromotedName: String
            get() = "PN"

    }

    override lateinit var pieceImage: PieceView
    private var commonID:Int = 0
    private var promotedID:Int = 0
    override var name: FigureName = FigureName.KNIGHT
    override var abbrName: String = Knight.abbrName
    override val promotable: Boolean = true
    override lateinit var rules: ShogiRules

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.knight_0
                promotedID = R.drawable.p_knight_0
            } else {
                commonID = R.drawable.knight_1
                promotedID = R.drawable.p_knight_1
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.knight_0
                promotedID = R.drawable.p_knight_0
            } else {
                commonID = R.drawable.knight_1
                promotedID = R.drawable.p_knight_1
            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.KNIGHT
        if(promoted){
            this.name = Knight.promotedName
            abbrName = Knight.abbrPromotedName
            rules = if (side == Side.WHITE){
                ShogiRules.GOLD_WHITE
            } else {
                ShogiRules.GOLD_BLACK
            }
        } else {
            this.name = Knight.name
            abbrName = Knight.abbrName
            rules = if (side == Side.WHITE){
                ShogiRules.PAWN_WHITE
            } else {
                ShogiRules.PAWN_BLACK
            }
        }
        pieceImage = pieceView
        appInfo.layout.addView(pieceView)
    }
    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.knight_1
                promotedID = R.drawable.p_knight_1
            } else {
                commonID = R.drawable.knight_0
                promotedID = R.drawable.p_knight_0
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.knight_1
                promotedID = R.drawable.p_knight_1
            } else {
                commonID = R.drawable.knight_0
                promotedID = R.drawable.p_knight_0
            }
        }
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

class Rook(override var side: Side, override var row: Int, override var col: Int,
           override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
           override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {

    companion object {
        val name: FigureName
            get() = FigureName.ROOK
        val promotedName: FigureName
            get() = FigureName.PROMOTED_ROOK
        val abbrName: String
            get() = "R"
        val abbrPromotedName: String
            get() = "+R"

    }

    override lateinit var pieceImage: PieceView
    private var commonID:Int = 0
    private var promotedID:Int = 0
    override var name: FigureName = Rook.name
    override var abbrName: String = Rook.abbrName
    override val promotable: Boolean = true

    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.rook_1
                promotedID = R.drawable.p_rook_1
            } else {
                commonID = R.drawable.rook_0
                promotedID = R.drawable.p_rook_0
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.rook_1
                promotedID = R.drawable.p_rook_1
            } else {
                commonID = R.drawable.rook_0
                promotedID = R.drawable.p_rook_0
            }
        }
    }

    override lateinit var rules: ShogiRules

   init {
       var pieceView = PieceView(appInfo.context,this)
       pieceView.setOnClickListener(listener)
       if (orientation == Orientation.NORMAL) {
           if (side == Side.WHITE) {
               commonID = R.drawable.rook_0
               promotedID = R.drawable.p_rook_0
           } else {
               commonID = R.drawable.rook_1
               promotedID = R.drawable.p_rook_1
           }
       } else {
           if (side == Side.WHITE) {
               commonID = R.drawable.rook_0
               promotedID = R.drawable.p_rook_1
           } else {
               commonID = R.drawable.rook_1
               promotedID = R.drawable.p_rook_1
           }
       }
       pieceView.setImageResource(commonID)
       pieceView.layoutParams = appInfo.layoutParams
       pieceView.x = x
       pieceView.y = y

       if(promoted){
           this.name = Rook.promotedName
           abbrName = Rook.abbrPromotedName
           rules = if (side == Side.WHITE){
               ShogiRules.DRAGON
           } else {
               ShogiRules.DRAGON
           }
       } else {
           this.name = Rook.name
           abbrName = Rook.abbrName
           rules = if (side == Side.WHITE){
               ShogiRules.ROOK
           } else {
               ShogiRules.ROOK
           }
       }
       pieceImage = pieceView
       appInfo.layout.addView(pieceView)
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

class Bishop(override var side: Side, override var row: Int, override var col: Int,
             override var promoted: Boolean, override val appInfo: AppInfo, x:Float, y:Float,
             override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {

    companion object {
        val name: FigureName
            get() = FigureName.BISHOP
        val promotedName: FigureName
            get() = FigureName.PROMOTED_BISHOP
        val abbrName: String
            get() = "B"
        val abbrPromotedName: String
            get() = "+B"

    }

    override lateinit var pieceImage: PieceView
    private var commonID:Int = 0
    private var promotedID:Int = 0
    override var name: FigureName = Bishop.name
    override var abbrName: String = Bishop.abbrName
    override val promotable: Boolean = true
    override fun changeImages() {
        if (orientation == Orientation.NORMAL) {
            if (side == Side.BLACK) {
                commonID = R.drawable.bishop_0
                promotedID = R.drawable.p_bishop_0
            } else {
                commonID = R.drawable.bishop_1
                promotedID = R.drawable.p_bishop_1
            }
        } else {
            if (side == Side.WHITE) {
                commonID = R.drawable.bishop_0
                promotedID = R.drawable.p_bishop_0
            } else {
                commonID = R.drawable.bishop_1
                promotedID = R.drawable.p_bishop_1
            }
        }
    }

    override lateinit var rules: ShogiRules

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                commonID = R.drawable.bishop_0
                promotedID = R.drawable.p_bishop_0
            } else {
                commonID = R.drawable.bishop_1
                promotedID = R.drawable.p_bishop_1
            }
        } else {
            if (side == Side.BLACK) {
                commonID = R.drawable.bishop_0
                promotedID = R.drawable.p_bishop_0
            } else {
                commonID = R.drawable.bishop_1
                promotedID = R.drawable.p_bishop_1
            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        if(promoted){
            this.name = Bishop.promotedName
            abbrName = Bishop.abbrPromotedName
            rules = ShogiRules.HORSE
        } else {
            this.name = Bishop.name
            abbrName = Bishop.abbrName
            rules = ShogiRules.BISHOP
        }
        pieceImage = pieceView
        appInfo.layout.addView(pieceView)
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

class King(override var side: Side, override var row: Int, override var col: Int,
           override val appInfo: AppInfo, x:Float, y:Float,
           override val orientation: Orientation,override var listener: View.OnClickListener) : Figure() {


    override var promoted: Boolean = false
    override fun changeImages() {
        commonID = if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                R.drawable.king_1
            } else {
                R.drawable.king_0
            }
        } else {
            if (side == Side.WHITE) {
                R.drawable.king_1
            } else {
                R.drawable.king_0

            }
        }
    }

    private var commonID:Int = 0
    override lateinit var pieceImage: PieceView
    override var name: FigureName = King.name
    override var abbrName: String = King.abbrName
    override val promotable: Boolean = false
    override var rules: ShogiRules = ShogiRules.KING

    init {
        var pieceView = PieceView(appInfo.context,this)
        pieceView.setOnClickListener(listener)
        commonID = if (orientation == Orientation.NORMAL) {
            if (side == Side.WHITE) {
                R.drawable.king_0
            } else {
                R.drawable.king_1
            }
        } else {
            if (side == Side.WHITE) {
                R.drawable.king_0
            } else {
                R.drawable.king_1

            }
        }
        pieceView.setImageResource(commonID)
        pieceView.layoutParams = appInfo.layoutParams
        pieceView.x = x
        pieceView.y = y

        this.name = FigureName.TOKIN

        this.name = Gold.name
        abbrName = Gold.abbrName
        rules = if (side == Side.WHITE) {
            ShogiRules.KING
        } else {
            ShogiRules.KING
        }
        pieceImage = pieceView
        appInfo.layout.addView(pieceView)
    }

    companion object {
        val name: FigureName
            get() = FigureName.KING
        val abbrName: String
            get() = "K"

    }

    override fun promote(): Boolean {
        return false
    }

    override fun reset() {}
}
