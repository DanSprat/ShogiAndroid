package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.*
import ru.popov.shogi.BuildConfig
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.DisplacementInfo
import ru.popov.shogi.classes.figures.Orientation
import ru.popov.shogi.classes.figures.Side
import ru.popov.shogi.databinding.FragmentShogiBinding
import java.io.IOException

class MultiplayerShogiFragment : Fragment() {
    private lateinit var inflater: LayoutInflater
    private var shogi: ShogiModelMultiplayer? = null
    var gson = Gson()

    private val bitmaps = HashMap<Int, Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentShogiBinding.inflate(inflater, container, false)
        val layout: RelativeLayout = binding.root.findViewById(R.id.layout_SHG)
        this.inflater = inflater

        val bl = ShogiBoardLayoutHelper.compute(resources, context?.theme)
        Log.i("Size", "Top: ${bl.top}, Left: ${bl.left} ")
        Log.i("Size", "Width: ${resources.displayMetrics.widthPixels}, Height: ${resources.displayMetrics.heightPixels} ")
        binding.noteSize = bl.noteSize
        binding.separateLineSize = bl.separateLineSize
        binding.boardSize = bl.boardSize

        val callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            override fun onResponse(call: Call, response: Response) {
                val isWhite = response.body()?.string()
                val clientWS = OkHttpClient()
                val request: Request = Request.Builder()
                    .url(BuildConfig.SHOGI_WS_URL)
                    .addHeader("SSH", "128")
                    .build()
                val webSocket = clientWS.newWebSocket(request, SocketListener())
                val side = if (isWhite == "false") Side.BLACK else Side.WHITE
                activity?.runOnUiThread {
                    shogi = activity?.let {
                        ShogiModelMultiplayer(
                            Orientation.NORMAL,
                            bl.topY.toFloat(),
                            bl.topX.toFloat(),
                            bl.noteSize,
                            bl.separateLineSize,
                            layout,
                            it,
                            bl.layoutParams,
                            bl.top.toFloat(),
                            bl.left.toFloat(),
                            webSocket,
                            side,
                            Side.WHITE
                        )
                    }
                }
            }
        }
        val client = OkHttpClient()
        val requestHttp = Request.Builder().url(BuildConfig.SHOGI_ROOM_URL).build()
        client.newCall(requestHttp).enqueue(callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()
    }

    inner class SocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.i("Moving:", text)
            val displacementInfo = gson.fromJson(text, DisplacementInfo::class.java)
            shogi?.movePieceAt(displacementInfo)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.i("Moving: ", " Closed $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i("Moving ", "Failure ${t.message}")
        }
    }
}
