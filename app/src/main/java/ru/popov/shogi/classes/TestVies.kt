package ru.popov.shogi.classes

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View
import ru.popov.shogi.R

class TestVies(context: Context?, attrs: AttributeSet): View(context,attrs) {


    init {
        context?.obtainStyledAttributes(attrs, R.styleable.TestVies).also {

        }
    }
    override fun onDraw(canvas: Canvas?) {
    }
}