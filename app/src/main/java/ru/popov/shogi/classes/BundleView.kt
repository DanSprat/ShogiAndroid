package ru.popov.shogi.classes

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.toBitmap
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.*

class BundleView(context: Context?, attrs: AttributeSet): View(context,attrs) {

    companion object {
        private var firstAdded = false
        private val hashMap:HashMap<String,Int> = HashMap()

        init {
            hashMap["Pawn"] = 0
            hashMap["Lance"] = 1
            hashMap["Knight"] = 2
            hashMap["Silver"] = 3
            hashMap["Gold"] = 4
            hashMap["Bishop"] = 5
            hashMap["Rook"] = 6
        }
    }

    var isLower:Int = 0
    var boardSZ:Int = 0
    var layoutParam:ViewGroup.LayoutParams
    var appInfo:AppInfo? = null
    var firstCellX = 0f
    var firstCellY = 0f
    var layoutParamsLittle:ViewGroup.LayoutParams
    private var leftCoord:Int = 0
    private var topCoord:Int = 0
    private var widthOneCell:Int = 0


    init {
        var draw = resources.getDrawable(R.drawable.bishop_0,context?.theme)
        layoutParam = ViewGroup.LayoutParams(draw.intrinsicWidth,draw.intrinsicHeight)
        layoutParamsLittle = ViewGroup.LayoutParams(draw.intrinsicWidth,draw.intrinsicHeight)
        context?.theme?.obtainStyledAttributes(attrs,R.styleable.BundleView,0,0).let {
            if (it != null) {
                isLower = it.getInt(R.styleable.BundleView_isLower,0)
            }
        }
    }

    fun moveToBundle(figure: Figure){
        if (!firstAdded) {
            layoutParamsLittle = figure.pieceImage.layoutParams
            firstAdded = true
        }

        figure.pieceImage.layoutParams = RelativeLayout.LayoutParams(layoutParam.width,layoutParam.height)
        Log.i("Class",figure.javaClass.simpleName)
        hashMap[figure.javaClass.simpleName].let {
            if (it != null) {
                figure.pieceImage.x = (leftCoord + it * widthOneCell).toFloat()
            }
        }
        figure.pieceImage.y = topCoord.toFloat()
    }
    override fun onDraw(canvas: Canvas?) {
        val paddingX = ((resources.displayMetrics.widthPixels - boardSZ) / 2 )
        val paddingY:Int = (resources.displayMetrics.heightPixels - boardSZ) / 2



        val relation:Int = (0.1 * paddingY).toInt()
        val paddingBundleX:Int = paddingX
        val paddingBundleY:Int = if (isLower == 1) {
            paddingY + boardSZ + relation
        } else {
            paddingY - relation
        }
        widthOneCell =  boardSZ / 7
        val widthOfPieceInside = widthOneCell * 0.9
        val test1 = resources.getDrawable(R.drawable.rook_0, context?.theme)
        var pieceRel = test1.intrinsicWidth / widthOfPieceInside

        layoutParam = ViewGroup.LayoutParams((test1.intrinsicWidth / pieceRel).toInt(),(test1.intrinsicHeight / pieceRel).toInt())
        var heightOnePiece = layoutParam.height / 0.85

        val paint = Paint()
        paint.color = Color.argb(75,26,26,26)
        paint.strokeWidth = 3f

        canvas?.drawRect(paddingBundleX.toFloat(),
            paddingBundleY.toFloat(),
            (paddingBundleX + widthOneCell * 7).toFloat(),
            (paddingBundleY + isLower * heightOnePiece).toFloat(),paint)

        var diffX = (widthOneCell - widthOfPieceInside) / 2
        var diffY = (heightOnePiece - layoutParam.height) / 2

        var startX = (paddingBundleX + diffX.toInt())
        var startY = (paddingBundleY + isLower * diffY.toInt())

        leftCoord = startX
        topCoord =  if (isLower == 1) startY else startY - layoutParam.height

        val matrixArr: FloatArray = floatArrayOf(1f,0f,0f,0f,0f,
            0f,1f,0f,0f,0f,
            0f,0f,1f,0f,0f,
            0f,0f,0f,0.6f,0f,
            0f,0f,0f,0f,0f)

        var bottomArray = arrayOf(R.drawable.pawn_0,R.drawable.lance_0,R.drawable.knight_0,R.drawable.silver_0,R.drawable.gold_0,R.drawable.bishop_0,R.drawable.rook_0)
        var upperArray = arrayOf(R.drawable.pawn_1,R.drawable.lance_1,R.drawable.knight_1,R.drawable.silver_1,R.drawable.gold_1,R.drawable.bishop_1,R.drawable.rook_1)
        paint.color = Color.BLACK
        if (isLower == 1) {
            for (i in 0..6){
                var drawable = resources.getDrawable(bottomArray[i],null).also {
                    it.colorFilter = ColorMatrixColorFilter(ColorMatrix(matrixArr))
                }
                canvas?.drawBitmap(drawable.toBitmap(),null, Rect((startX + i * widthOneCell),topCoord,
                    (startX + i * widthOneCell + layoutParam.width),topCoord + layoutParam.height),paint)
            }
        } else {
            for (i in 0..6){
                var drawable = resources.getDrawable(upperArray[i],null).also {
                    it.colorFilter = ColorMatrixColorFilter(ColorMatrix(matrixArr))
                }
                canvas?.drawBitmap(drawable.toBitmap(),null, Rect((startX + i * widthOneCell),topCoord,
                    (startX + i * widthOneCell + layoutParam.width),topCoord + layoutParam.height),paint)
            }
        }

    }
}