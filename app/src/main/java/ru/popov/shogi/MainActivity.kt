package ru.popov.shogi

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import okhttp3.*
import org.json.JSONObject
import ru.popov.shogi.classes.ShogiRules

import okhttp3.OkHttpClient
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.text.format.Formatter.formatIpAddress
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentTransaction
import ru.popov.shogi.classes.MainMenuFragment
import ru.popov.shogi.classes.MultiplayerShogiFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var webSocket: WebSocket

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        var fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout,MainMenuFragment())
        fragmentTransaction.commit()
        MediaPlayer.create(this,R.raw.shogitheme).also {
            it.isLooping = true
            //it.start()
        }

    }




}