package ru.popov.shogi

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import ru.popov.shogi.classes.ShogiRules

class MainActivity : AppCompatActivity() {
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        MediaPlayer.create(this,R.raw.shogitheme).also {
            it.isLooping = true
            it.start()
        }
    }
}