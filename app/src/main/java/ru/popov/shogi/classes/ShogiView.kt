package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ru.popov.shogi.R

class ShogiView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    var separateLineSize:Int =0
    var noteSize:Int = 0

    private final val relation = 0.10

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()

        val noteSize = this.noteSize
        val separateLineSize:Int = this.separateLineSize
        val boardSize = 9 * noteSize + 10*separateLineSize
        val paddingX = ((resources.displayMetrics.widthPixels - boardSize) / 2 )

        val board = BitmapFactory.decodeResource(resources,R.drawable.board)
        val paddingY:Int = (resources.displayMetrics.heightPixels - boardSize) / 2

        val paddingFloatX = paddingX.toFloat()
        val paddingFloatY = paddingY.toFloat()


        Log.i("Size", "Top: $paddingY, Left: $paddingX ")
        Log.i("Size", "Width: ${resources.displayMetrics.widthPixels}, Height: ${resources.displayMetrics.heightPixels} ")
        canvas?.drawBitmap(board,null,Rect(paddingX,paddingY,paddingX + boardSize,paddingY+boardSize),paint)
        paint.color = Color.rgb(0,0,0)
        paint.style = Paint.Style.FILL
        val step:Int = separateLineSize + noteSize

        for(i in 0..9){
            canvas?.drawRect(paddingFloatX + i*step ,paddingFloatY,paddingFloatX + separateLineSize + i*step,paddingFloatY+boardSize,paint)
            canvas?.drawRect(paddingFloatX,paddingFloatY + i*step,paddingFloatX + boardSize,paddingFloatY +separateLineSize +i* step,paint)
        }
    }





}