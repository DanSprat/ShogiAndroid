package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import ru.popov.shogi.R

class ShogiView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    private final val originX = 20f
    private final val originY = 130f
    var separateLineSize:Int =0
    var noteSize:Int = 0
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
    private final val relation = 0.10
    init {
        loadBitmaps()
    }
    @SuppressLint("ResourceType")
    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()

        val attrs = context.obtainStyledAttributes(R.styleable.ShogiView)
        val noteSize = this.noteSize
        val separateLineSize:Int = this.separateLineSize
        val boardSize = 9 * noteSize + 10*separateLineSize
        val paddingX = ((width - boardSize) / 2 )

        val gold = BitmapFactory.decodeResource(resources, R.drawable.gold)
        val board = BitmapFactory.decodeResource(resources,R.drawable.board)
        val paddingY:Int = (height - boardSize) / 2

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