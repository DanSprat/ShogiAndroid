package ru.popov.shogi.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ru.popov.shogi.classes.figures.Orientation

class ViewMoves(context:Context ,attrs: AttributeSet):View(context,attrs) {
    var cellSize:Int = 0
    var lineSize:Int =0
    var boardSize:Int = 0
    var orientation:Orientation = Orientation.NORMAL
    var scaleX:Int = 1
    var scaleY = 1
    var clean:Boolean = false
    private var top = 0f
    get() = ((resources.displayMetrics.heightPixels - boardSize) / 2).toFloat()

    private var left:Float = 0f
    get() = ((resources.displayMetrics.widthPixels - boardSize) / 2 ).toFloat()

    var firstCellX = 0f
    var firstCellY = 0f
    var movesSet = HashSet<Pair<Int,Int>> ()

    override fun onDraw(canvas: Canvas?) {
        if(!clean) {
            var paddingX = 0f
            var paddingY = 0f
            left = ((resources.displayMetrics.widthPixels - boardSize) / 2 ).toFloat()
            if (orientation == Orientation.NORMAL) {
                firstCellX = left + cellSize / 2 + lineSize
                firstCellY = top + boardSize - (cellSize / 2 + lineSize)
            } else {
                firstCellX = left + boardSize
                firstCellY = top
            }
            var delta = cellSize + lineSize
           val paint = Paint()
            paint.color = Color.GREEN
            paint.strokeWidth = 3f
           for (x in movesSet){
               for (i in movesSet){
                   canvas?.drawCircle(firstCellX + scaleX * delta * (x.first - 1), firstCellY + scaleY * delta * (x.second - 1),20f,paint)
               }
           }

        }
    }
}