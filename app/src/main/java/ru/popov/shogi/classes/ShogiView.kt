package ru.popov.shogi.classes

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ru.popov.shogi.R

class ShogiView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    private final val originX = 20f
    private final val originY = 130f

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        val viewWidth = this.width;
        val viewHeight = this.height
        val gold = BitmapFactory.decodeResource(resources, R.drawable.gold)
        val board = BitmapFactory.decodeResource(resources,R.drawable.board)

        val boardHeight = board.height
        val boardWidth = board.width

        val posX = viewWidth / 2 - boardWidth / 2
        val posY = viewHeight / 2 - boardHeight / 2
        canvas?.drawBitmap(board,null,Rect(posX,posY,posX + boardWidth,posY + boardHeight),paint)
        canvas?.drawBitmap(gold,null, Rect(0,0,100,100),paint)
    }
}