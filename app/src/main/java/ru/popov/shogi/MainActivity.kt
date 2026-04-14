package ru.popov.shogi

import android.annotation.SuppressLint
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import ru.popov.shogi.classes.MainMenuFragment

class MainActivity : AppCompatActivity() {

    /** Фоновая музыка: `res/raw/shogi.ogg` (можно положить `shogi.ogg` в корень проекта и скопировать в `res/raw`). */
    private var bgm: MediaPlayer? = null

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
            window.isStatusBarContrastEnforced = false
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = Color.TRANSPARENT
        }
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout, MainMenuFragment())
        fragmentTransaction.commit()

        bgm = MediaPlayer.create(this, R.raw.shogi)
            ?: MediaPlayer.create(this, R.raw.shogi_theme)
            ?: MediaPlayer.create(this, R.raw.shogitheme)
        bgm?.apply {
            isLooping = true
            setOnErrorListener { _, _, _ ->
                true
            }
            start()
        }
    }

    override fun onResume() {
        super.onResume()
        bgm?.let { if (!it.isPlaying) it.start() }
    }

    override fun onPause() {
        super.onPause()
        bgm?.pause()
    }

    override fun onDestroy() {
        bgm?.release()
        bgm = null
        super.onDestroy()
    }
}
