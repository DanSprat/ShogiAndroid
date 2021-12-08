package ru.popov.shogi.classes

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.popov.shogi.R

class ShogiView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    private final val originX = 20f
    private final val originY = 130f
    private final val imagesFig = setOf(
        R.drawable.bishop,
        R.drawable.gold,
        R.drawable.king,
        R.drawable.knight,
        R.drawable.lance,
        R.drawable.pawn,
        R.drawable.rook,
        R.drawable.silver
    )
    private final val bitmaps = HashMap<Int,Bitmap> ()
    private final val relation = 0.16
    init {
        loadBitmaps()
    }
    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        val viewWidth = this.width;
        val viewHeight = this.height

        var paddingX:Int = ((viewWidth * 0.1).toInt() / 2)
        var boardSize:Int = viewWidth - paddingX
        val noteSize:Int = (boardSize / (10 * relation + 9)).toInt()
        val separateLineSize:Int = (0.10 * noteSize).toInt()
        boardSize = 9 * noteSize + 10*separateLineSize
        paddingX = ((viewWidth - boardSize) / 2 )

        val gold = BitmapFactory.decodeResource(resources, R.drawable.gold)
        val board = BitmapFactory.decodeResource(resources,R.drawable.board)
        val paddingY:Int = (viewHeight - boardSize) / 2

        val paddingFloatX = paddingX.toFloat()
        val paddingFloatY = paddingY.toFloat()

        canvas?.drawBitmap(board,null,Rect(paddingX,paddingY,paddingX + boardSize,paddingY+boardSize),paint)
        canvas?.drawBitmap(gold,null, Rect(0,0,100,100),paint)
        paint.color = Color.rgb(0,0,0)
        paint.style = Paint.Style.FILL
        val step:Int = separateLineSize + noteSize

        for(i in 0..9){
            canvas?.drawRect(paddingFloatX + i*step ,paddingFloatY,paddingFloatX + separateLineSize + i*step,paddingFloatY+boardSize,paint)
            canvas?.drawRect(paddingFloatX,paddingFloatY + i*step,paddingFloatX + boardSize,paddingFloatY +separateLineSize +i* step,paint)
        }

    }

    private fun loadBitmaps(){
        imagesFig.forEach { bitmaps[it] = BitmapFactory.decodeResource(resources,it) }
    }
}