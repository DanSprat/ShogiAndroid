package ru.popov.shogi.classes

import android.app.usage.UsageEvents
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*

class ShogiModel(var orientation: Orientation, var top:Float, var left:Float, var noteSize:Int, var separateLineSize:Int, var layout: RelativeLayout,
                 private var context: Context,private var layoutParams: ViewGroup.LayoutParams) {
    private val figuresOnBoard:MutableSet<Figure> = mutableSetOf()
    private val handWhite:ArrayList<Figure> = ArrayList()
    private val handBlack:ArrayList<Figure> = ArrayList()
    
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
            -1
        } else {
            -1
        }
    }

    private fun searchRoots(figure: Figure):HashSet<Pair<Int,Int>> {
        val moves:HashSet<Pair<Int,Int>> = HashSet()
        var currentX = figure.col
        var currentY = figure.row
        val rules = figure.rules
        for (i in 0 until rules.size){
            val vectorMove = rules[i]
            var x = vectorMove.vector.first
            var y = vectorMove.vector.second
            var length = 0
            for(j in 0 until vectorMove.length){
                if (currentX + x <1 || currentX + x > 9 || currentY + y < 1 || currentY + y >9 ||
                    boardShogi[currentX + x,currentY + y]?.side == figure.side ) break
                currentX+=x
                currentY+=y
                length++
            }
            if(length!=0){
                moves.add(Pair(currentX,currentY))
            }
            currentX= figure.col
            currentY = figure.row
        }
        return moves
    }

    private fun reset(){
        figuresOnBoard.clear();
        handBlack.clear()
        handWhite.clear()


        val appInfo = AppInfo(context, layout, layoutParams)
        val delta = separateLineSize + noteSize
        val scaleX  = getXScale(orientation,top, left)
        val scaleY = getYScale(orientation, top, left)
        var firstCellX = 0f
        var firstCellY = 0f
        if (orientation == Orientation.NORMAL){
            firstCellX = left
            firstCellY = top + delta * 8
        } else {
            firstCellX = left + 8 * delta
            firstCellY = top
        }
        var moves: Deferred<HashSet<Pair<Int,Int>>>? = null
        var checkedView:View? = null
        var firstTouch = true
        val touchListener:View.OnClickListener = View.OnClickListener { v->
            v.bringToFront()


            if (checkedView == null || checkedView != v) {
                if(checkedView != null && checkedView != v) {
                    layout.findViewById<ViewMoves>(R.id.available_moves).also {
                        it.clean = true
                        it.invalidate()
                    }
                    (checkedView as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix())
                }
                checkedView = v
                firstTouch = false
                val matrixArr: FloatArray = floatArrayOf(0f,0f,0f,0f,0f,
                    0f,1f,0f,0f,0f,
                    0f,0f,0f,0f,0f,
                    0f,0f,0f,0.5f,0f,
                    0f,0f,0f,0f,0f)

                (v as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix(matrixArr))
                runBlocking {
                    moves = async { searchRoots((v as PieceView).figure) }
                    launch {
                        val waitMoves = moves?.await()
                        if (waitMoves != null) {
                            layout.findViewById<ViewMoves>(R.id.available_moves).also {
                                it.movesSet = waitMoves
                                it.scaleX = scaleX
                                it.scaleY = scaleY
                                it.clean = false
                                it.invalidate()
                            }
                        }
                    }
                }
            } else {
                layout.findViewById<ViewMoves>(R.id.available_moves).also {
                    it.clean = true
                    it.invalidate()
                    (v as ImageView).colorFilter = ColorMatrixColorFilter(ColorMatrix())
                    checkedView = null
                }
            }

        }

        
        // Adding pawns
        for (i in 0..8){
            boardShogi[i+1,3] = Pawn(Side.WHITE,3,i+1,false,appInfo ,firstCellX + scaleX * (i) * delta,firstCellY + scaleY * 2 * delta,orientation,touchListener)
            boardShogi[i+1,7] = Pawn(Side.BLACK,7,i+1,false, appInfo,firstCellX + scaleX * (i) * delta,firstCellY + scaleY * 6 * delta,orientation,touchListener)
        }

        // Adding Silvers
        boardShogi[3,1] = Silver(Side.WHITE,1,3,false,appInfo,firstCellX + scaleX * 2 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[7,1] = Silver(Side.WHITE,1,7,false,appInfo,firstCellX + scaleX * 6 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[3,9] = Silver(Side.BLACK,9,3,false,appInfo,firstCellX + scaleX * 2 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)
        boardShogi[7,9] = Silver(Side.BLACK,9,7,false,appInfo,firstCellX + scaleX * 6 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)

        //Adding Golds
        boardShogi[4,1] = Gold(Side.WHITE,1,4,appInfo,firstCellX + scaleX * 3 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[6,1] = Gold(Side.WHITE,1,6,appInfo,firstCellX + scaleX * 5 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[4,9] = Gold(Side.BLACK,9,4,appInfo,firstCellX + scaleX * 3 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)
        boardShogi[6,9] = Gold(Side.BLACK,9,6,appInfo,firstCellX + scaleX * 5 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)

        //Adding Lances
        boardShogi[1,1] = Lance(Side.WHITE,1,1,false,appInfo,firstCellX + scaleX * 0 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[9,1] = Lance(Side.WHITE,1,9,false,appInfo,firstCellX + scaleX * 8 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[1,9] = Lance(Side.BLACK,9,1,false,appInfo,firstCellX + scaleX * 0 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)
        boardShogi[9,9] = Lance(Side.BLACK,9,9,false,appInfo,firstCellX + scaleX * 8 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)

        //Adding Kings
        boardShogi[5,1] = King(Side.WHITE,1,5,appInfo,firstCellX + scaleX * 4 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[5,9] = King(Side.BLACK,9,5,appInfo,firstCellX + scaleX * 4 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)

        //Adding Knights
        boardShogi[2,1] = Knight(Side.WHITE,1,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[8,1] = Knight(Side.WHITE,1,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*0*delta,orientation,touchListener)
        boardShogi[2,9] = Knight(Side.BLACK,9,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)
        boardShogi[8,9] = Knight(Side.BLACK,9,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*8*delta,orientation,touchListener)

        //Adding Rooks
        boardShogi[8,2] = Rook(Side.WHITE,2,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*1*delta,orientation,touchListener)
        boardShogi[2,8] = Rook(Side.BLACK,8,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*7*delta,orientation,touchListener)

        //Adding Bishops
        boardShogi[2,2] = Bishop(Side.WHITE,2,2,false,appInfo,firstCellX + scaleX * 1 *delta,firstCellY+scaleY*1*delta,orientation,touchListener)
        boardShogi[8,8] = Bishop(Side.BLACK,8,8,false,appInfo,firstCellX + scaleX * 7 *delta,firstCellY+scaleY*7*delta,orientation,touchListener)

    }

    fun createImageViews(context:Context,left:Int,bottom:Int,delta:Int,size:Int):ArrayList<ImageView> {
        val pieces:ArrayList<ImageView> = ArrayList();
        return pieces
    }
}