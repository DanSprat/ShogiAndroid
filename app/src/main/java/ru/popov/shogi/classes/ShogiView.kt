package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import ru.popov.shogi.R

class ShogiView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    var separateLineSize:Int =0
    var noteSize:Int = 0

    private final val relation = 0.10

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()

        Log.i("MYTAG","DMW " +resources.displayMetrics.widthPixels.toString())
        Log.i("MYTAG","DMH " +resources.displayMetrics.heightPixels.toString())
        val noteSize = this.noteSize
        val separateLineSize:Int = this.separateLineSize
        val boardSize = 9 * noteSize + 10*separateLineSize
        val paddingX = ((resources.displayMetrics.widthPixels - boardSize) / 2 )

        val board = BitmapFactory.decodeResource(resources,R.drawable.board)
        val paddingY:Int = (resources.displayMetrics.heightPixels - boardSize) / 2

        val paddingFloatX = paddingX.toFloat()
        val paddingFloatY = paddingY.toFloat()

        Log.i("MYTAG", "Width View $width")
        Log.i("MYTAG", "Height View $height")

        Log.i("MYTAG", "NewTOP: $paddingY")
        Log.i("MYTAG", "NewLeft: $paddingX")

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