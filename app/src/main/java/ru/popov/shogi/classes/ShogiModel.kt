package ru.popov.shogi.classes

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*

class ShogiModel(var orientation: Orientation, var top:Float, var left:Float, var noteSize:Int, var separateLineSize:Int, var layout: RelativeLayout,
                 private var context: Context,private var layoutParams: ViewGroup.LayoutParams) {
    private val figuresOnBoard:MutableSet<Figure> = mutableSetOf()
    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()

    private var board:Array<Array<Figure?>> = Array<Array<Figure?>>(9) { Array(9) { null } }
    private var boardShogi:BoardArray = BoardArray()

    init {
        reset()
    }
    
    private fun getXScale(orientation: Orientation,top:Float,left:Float):Int{
        return if (orientation == Orientation.NORMAL) {
            1
        } else {
            -1
        }
    }

    private fun getYScale(orientation: Orientation,top:Float,left:Float):Int{
        return if (orientation == Orientation.NORMAL) {
            1
        } else {
            -1
        }
    }
    private fun reset(){
        figuresOnBoard.clear();
        handBlack.clear()
        handWhite.clear()

        val appInfo = AppInfo(context, layout, layoutParams)
        val delta = separateLineSize + noteSize
        val scaleX  = getXScale(orientation,top, left)
        val scaleY = getYScale(orientation, top, left)
        // Adding pawns
        for (i in 0..8){
            // board[6][i] = Pawn(Side.WHITE,3,i,false,R.drawable.pawn_0,context,noteSize,left + scaleX * (i) * delta,top + scaleY * 6 * delta,layout,layoutParams)
            boardShogi[i+1,3] = Pawn(Side.WHITE,3,i+1,false,appInfo ,left + scaleX * (i) * delta,top + scaleY * 6 * delta,orientation)
            boardShogi[i+1,7] = Pawn(Side.BLACK,7,i+1,false, appInfo,left + scaleX * (i) * delta,top + scaleY * 2 * delta,orientation)
        }

        // Adding Silvers
        boardShogi[3,1] = Silver(Side.WHITE,1,3,false,appInfo,left + scaleX * 2 *delta,top+scaleY*8*delta,orientation)
        boardShogi[7,1] = Silver(Side.WHITE,1,7,false,appInfo,left + scaleX * 6 *delta,top+scaleY*8*delta,orientation)
        boardShogi[3,9] = Silver(Side.BLACK,9,3,false,appInfo,left + scaleX * 2 *delta,top+scaleY*0*delta,orientation)
        boardShogi[7,9] = Silver(Side.BLACK,9,7,false,appInfo,left + scaleX * 6 *delta,top+scaleY*0*delta,orientation)

        //Adding Golds
        boardShogi[4,1] = Gold(Side.WHITE,1,4,appInfo,left + scaleX * 3 *delta,top+scaleY*8*delta,orientation)
        boardShogi[6,1] = Gold(Side.WHITE,1,6,appInfo,left + scaleX * 5 *delta,top+scaleY*8*delta,orientation)
        boardShogi[4,9] = Gold(Side.BLACK,9,4,appInfo,left + scaleX * 3 *delta,top+scaleY*0*delta,orientation)
        boardShogi[6,9] = Gold(Side.BLACK,9,6,appInfo,left + scaleX * 5 *delta,top+scaleY*0*delta,orientation)

        //Adding Lances
        boardShogi[1,1] = Lance(Side.WHITE,1,1,false,appInfo,left + scaleX * 0 *delta,top+scaleY*8*delta,orientation)
        boardShogi[9,1] = Lance(Side.WHITE,1,9,false,appInfo,left + scaleX * 8 *delta,top+scaleY*8*delta,orientation)
        boardShogi[1,9] = Lance(Side.BLACK,9,1,false,appInfo,left + scaleX * 0 *delta,top+scaleY*0*delta,orientation)
        boardShogi[9,9] = Lance(Side.BLACK,9,9,false,appInfo,left + scaleX * 8 *delta,top+scaleY*0*delta,orientation)

        //Adding Kings
        boardShogi[5,1] = King(Side.WHITE,1,5,appInfo,left + scaleX * 4 *delta,top+scaleY*8*delta,orientation)
        boardShogi[5,9] = King(Side.BLACK,9,5,appInfo,left + scaleX * 4 *delta,top+scaleY*0*delta,orientation)

    }

    fun createImageViews(context:Context,left:Int,bottom:Int,delta:Int,size:Int):ArrayList<ImageView> {
        val pieces:ArrayList<ImageView> = ArrayList();
        return pieces
    }
}