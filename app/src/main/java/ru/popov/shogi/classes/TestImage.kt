package ru.popov.shogi.classes

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class TestImage(context: Context, attrs: AttributeSet):AppCompatImageView(context,attrs) {
    var cellSize:Int = 0
    var lineSize:Int =0
    var boardSize:Int = 0

}