package ru.popov.shogi.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class WhiteCanvasView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    var line:Int = 0
    var cell:Int = 0
    var boardWidth:Int = 0
    var clean:Boolean = true
    var newPoint:Pair<Float,Float> = Pair (0f,0f)
    var otherPoint:Pair<Float,Float> = Pair (0f,0f)
    var layoutParam = ViewGroup.LayoutParams(0,0)
    private var top = 0f
        get() = ((resources.displayMetrics.heightPixels - boardWidth) / 2).toFloat()

    private var left:Float = 0f
        get() = ((resources.displayMetrics.widthPixels - boardWidth) / 2 ).toFloat()
    override fun onDraw(canvas: Canvas?) {
        if(!clean){
            var paint:Paint = Paint()
            paint.color = Color.argb(150,255,255,255)
            canvas?.drawRect(left,top,left+boardWidth,top+boardWidth,paint)
            paint.color = Color.argb(150,255,0,0)
            var diffX:Float = (cell - layoutParam.width) / 2f
            var diffY:Float = (cell - layoutParam.height) / 2f
            canvas?.drawRect(newPoint.first - diffX ,newPoint.second - diffY ,newPoint.first -diffX + cell,newPoint.second-diffY+cell,paint)
            paint.color = Color.argb(150,0,0,0)
            canvas?.drawRect(otherPoint.first-diffX,otherPoint.second-diffY,otherPoint.first-diffX + cell,otherPoint.second- diffY +cell,paint)
        }
    }
}