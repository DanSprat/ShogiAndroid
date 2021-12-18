package ru.popov.shogi.classes

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*

class ShogiModel(var orientation: Orientation, var top:Float, var left:Float, var noteSize:Int, var separateLineSize:Int, var layout: RelativeLayout,
                 private var context: Context) {
    private val figuresOnBoard:MutableSet<Figure> = mutableSetOf()
    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()

    private var board:Array<Array<Figure?>> = Array<Array<Figure?>>(9) { Array(9) { null } }

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

        val some = context.resources.getDrawable(R.drawable.king,context.theme)
        var relation = some.intrinsicHeight.toFloat() / noteSize
        var layoutParams = if (relation > 1){
            ViewGroup.LayoutParams((some.intrinsicWidth.toFloat() / relation).toInt(),(some.intrinsicHeight.toFloat() / relation).toInt())
        } else {
            ViewGroup.LayoutParams((some.intrinsicWidth.toFloat() * relation).toInt(),(some.intrinsicHeight.toFloat() * relation).toInt())
        }

        var newTop:Float = top - layoutParams.height / 2
        var newLeft:Float = left - layoutParams.width / 2

        val delta = separateLineSize + noteSize
        val scaleX  = getXScale(orientation,top, left)
        val scaleY = getYScale(orientation, top, left)
        // Adding pawns
        for (i in 0..8){
            board[6][i] = Pawn(Side.WHITE,3,i,false,R.drawable.ic__fu,context,noteSize,left + scaleX * (i-1) * delta,top + scaleY * 5 * delta,layout,layoutParams)
        }

    }

    fun createImageViews(context:Context,left:Int,bottom:Int,delta:Int,size:Int):ArrayList<ImageView> {
        val pieces:ArrayList<ImageView> = ArrayList();
        return pieces
    }
}