package ru.popov.shogi.classes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.R

import android.content.res.TypedArray
import android.util.Log
import androidx.databinding.BindingAdapter
import ru.popov.shogi.classes.figures.Figure


class CountsView(context: Context?, attrs: AttributeSet): View(context,attrs) {
    var inLow:Int = 0
    var board:Int = 0

    init {
        var attributes = context?.obtainStyledAttributes(attrs,ru.popov.shogi.R.styleable.CountsView)
        attributes?.let { ta ->
            ta.getInteger(ru.popov.shogi.R.styleable.CountsView_inLow,0)?.let {
                inLow = it
            }
        }
    }

    private class Info(var position:Int,var count:Int)
    private val hashMap: HashMap<String,Info> = HashMap()
    init {
        hashMap["Pawn"] = Info(0,0)
        hashMap["Lance"] = Info(1,0)
        hashMap["Knight"] = Info(2,0)
        hashMap["Silver"] = Info(3,0)
        hashMap["Gold"] = Info(4,0)
        hashMap["Bishop"] = Info(5,0)
        hashMap["Rook"] = Info(6,0)
    }


    override fun onDraw(canvas: Canvas?) {
        var mTextBoundRect:Rect = Rect()
        var string:String = "1"
        val paddingX = ((resources.displayMetrics.widthPixels - board) / 2 )
        val paddingY:Int = (resources.displayMetrics.heightPixels - board) / 2
        val relation:Int = (0.1 * paddingY).toInt()
        var countBoxSize:Int = (relation * 0.8).toInt()
        var marginTop:Int = (relation - countBoxSize) / 2
        var widthOneCell:Int = board / 7
        var marginLeft:Int = (widthOneCell - countBoxSize) / 2
        val paddingBundleX:Int = paddingX
        val paddingBundleY:Int = if (inLow == 1) {
            paddingY + board + marginTop
        } else {
            paddingY - marginTop - countBoxSize
        }
        var paint = Paint()
        var paintText = Paint()
        paint.color = Color.argb(200,235,125,0)
        paintText.textSize = countBoxSize *0.8f
        paintText.color = Color.argb(250,255,255,255)


        for(x in hashMap.entries) {
            var info = x.value
            if (info.count > 0){
                canvas?.drawRect(Rect(paddingBundleX + marginLeft + info.position * widthOneCell,paddingBundleY,paddingBundleX + marginLeft + info.position * widthOneCell + countBoxSize,paddingBundleY + countBoxSize),paint)
                paintText.getTextBounds(string,0,string.length,mTextBoundRect)
                var textWidth = paintText.measureText(string)
                var textHeight = mTextBoundRect.height()
                canvas?.drawText(info.count.toString(), ((paddingBundleX + widthOneCell / 2 - textWidth / 2 + info.position * widthOneCell)),
                    (paddingBundleY + countBoxSize - (countBoxSize - textHeight) / 2).toFloat(),paintText)
            }
        }
    }
    fun increment(figure: Figure){
        hashMap[figure::class.java.simpleName]?.let {
            it.count++
        }
        invalidate()
    }

    fun decrement(figure: Figure){
        hashMap[figure::class.java.simpleName]?.let {
            it.count--
        }
        invalidate()

    }

}