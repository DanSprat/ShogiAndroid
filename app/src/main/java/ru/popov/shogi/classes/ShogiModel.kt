package ru.popov.shogi.classes

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*

class ShogiModel {
    private val figuresOnBoard:MutableSet<Figure> = mutableSetOf()
    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()
    private lateinit var context: Context
    private lateinit var bundleUp:Any
    private lateinit var bundleDown:Any
    private var board:Array<Array<Figure?>> = Array<Array<Figure?>>(9) { Array(9) { null } }

    init {
        reset()
    }


    private fun transformCoordinateToIndex(mySide: Side,i:Int,y:Int){

    }
    private fun transformIndexToCoordinate(mySide: Side,i:Int,y:Int){

    }
    fun connect(context:Context,layout: RelativeLayout,top:Float,left:Float,bundle1:RecyclerView?,bundle2: RecyclerView?,figureSize:Int,mySide: Side,loadBundle:Any?){
        this.context = context
        if (loadBundle == null) {
            reset()
            for (x in figuresOnBoard){
                x.setImage(context,layout,)
            }
        }
    }

    private fun reset(){
        figuresOnBoard.clear();
        handBlack.clear()
        handWhite.clear()

        // Adding pawns
        for (i in 0..8){
            board[2][i] = Pawn(Side.BLACK).apply { figuresOnBoard.add(this) }
            board[6][i] = Pawn(Side.WHITE).apply { figuresOnBoard.add(this) }
        }

    }

    fun createImageViews(context:Context,left:Int,bottom:Int,delta:Int,size:Int):ArrayList<ImageView> {
        val pieces:ArrayList<ImageView> = ArrayList();
        return pieces
    }
}