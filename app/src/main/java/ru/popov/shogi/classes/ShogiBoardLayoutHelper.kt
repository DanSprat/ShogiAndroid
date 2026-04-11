package ru.popov.shogi.classes

import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.ViewGroup
import ru.popov.shogi.R

data class ShogiBoardLayout(
    val noteSize: Int,
    val separateLineSize: Int,
    val boardSize: Int,
    val top: Int,
    val left: Int,
    val topX: Int,
    val topY: Int,
    val layoutParams: ViewGroup.LayoutParams
)

object ShogiBoardLayoutHelper {

    fun compute(
        resources: Resources,
        theme: Resources.Theme?,
        metrics: DisplayMetrics = resources.displayMetrics,
        scaleNote: Float = 0.9f,
        relation: Float = 0.1f
    ): ShogiBoardLayout {
        val displayWidth = metrics.widthPixels
        val paddingX = ((displayWidth * 0.1).toInt() / 2)
        var boardSize = displayWidth - paddingX
        val noteSize = (boardSize / (10 * relation + 9)).toInt()
        val separateLineSize = (relation * noteSize).toInt()
        boardSize = 10 * separateLineSize + 9 * noteSize

        val pieceDrawable = resources.getDrawable(R.drawable.rook_0, theme)
        val rel = pieceDrawable.intrinsicHeight.toFloat() / (noteSize * scaleNote)
        val layoutParams = ViewGroup.LayoutParams(
            (pieceDrawable.intrinsicWidth / rel).toInt(),
            (pieceDrawable.intrinsicHeight / rel).toInt()
        )

        val top = (metrics.heightPixels - boardSize) / 2
        val left = (metrics.widthPixels - boardSize) / 2
        val topX = (displayWidth - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.width / 2
        val topY = (metrics.heightPixels - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.height / 2

        return ShogiBoardLayout(
            noteSize,
            separateLineSize,
            boardSize,
            top,
            left,
            topX,
            topY,
            layoutParams
        )
    }
}
