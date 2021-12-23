package ru.popov.shogi.classes

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import ru.popov.shogi.classes.figures.Figure

class PieceView(context:Context,var figure:Figure):androidx.appcompat.widget.AppCompatImageView(context) {


    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }




}