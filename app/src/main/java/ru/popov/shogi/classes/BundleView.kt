package ru.popov.shogi.classes

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.Orientation

class BundleView(context: Context?, attrs: AttributeSet): View(context,attrs) {

    var isLower:Int = 0
    var boardSZ:Int = 0

    init {
        context?.theme?.obtainStyledAttributes(attrs,R.styleable.BundleView,0,0).let {
            if (it != null) {
                isLower = it.getInt(R.styleable.BundleView_isLower,0)
            }
        }
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
        val widthOneCell:Int =  boardSZ / 7
        val widthOfPieceInside = widthOneCell * 0.9
        val test1 = resources.getDrawable(R.drawable.rook_0, context?.theme)
        var pieceRel = test1.intrinsicWidth / widthOfPieceInside

        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams((test1.intrinsicWidth / pieceRel).toInt(),(test1.intrinsicHeight / pieceRel).toInt())
        var heightOnePiece = layoutParams.height / 0.85

        val paint = Paint()
        paint.color = Color.argb(75,26,26,26)
        paint.strokeWidth = 3f

        canvas?.drawRect(paddingBundleX.toFloat(),
            paddingBundleY.toFloat(),
            (paddingBundleX + widthOneCell * 7).toFloat(),
            (paddingBundleY + isLower * heightOnePiece).toFloat(),paint)

        var diffX = (widthOneCell - widthOfPieceInside) / 2
        var diffY = (heightOnePiece - layoutParams.height) / 2

        var startX = (paddingBundleX + diffX.toInt())
        var startY = (paddingBundleY + isLower * diffY.toInt())
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
                canvas?.drawBitmap(drawable.toBitmap(),null, Rect((startX + i * widthOneCell),startY,
                    (startX + i * widthOneCell + layoutParams.width),startY + layoutParams.height),paint)
            }
        } else {
            for (i in 0..6){
                var drawable = resources.getDrawable(upperArray[i],null).also {
                    it.colorFilter = ColorMatrixColorFilter(ColorMatrix(matrixArr))
                }
                canvas?.drawBitmap(drawable.toBitmap(),null, Rect((startX + i * widthOneCell),startY - layoutParams.height,
                    (startX + i * widthOneCell + layoutParams.width),startY),paint)
            }
        }

    }
}