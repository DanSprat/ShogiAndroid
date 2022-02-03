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
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.DisplacementInfo
import ru.popov.shogi.classes.figures.Orientation
import ru.popov.shogi.classes.figures.Side
import ru.popov.shogi.databinding.FragmentShogiBinding

class MultiplayerShogiFragment:Fragment() {
    private lateinit var inflater: LayoutInflater
    private var shogi: ShogiModelMultiplayer? = null
    var gson = Gson()

    private final val bitmaps = HashMap<Int, Bitmap> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentShogiBinding = FragmentShogiBinding.inflate(inflater,container,false)
        val layout: RelativeLayout = binding.root.findViewById(R.id.layout_SHG)
        val scaleNote:Float = 0.9f
        val relation:Float = 0.1f


        val dm = resources.displayMetrics
        val displayWidth = dm.widthPixels
        this.inflater = inflater
        var paddingX:Int = ((displayWidth*0.1).toInt() / 2)
        var boardSize:Int = displayWidth - paddingX
        val noteSize:Int = (boardSize / (10 * relation + 9)).toInt()
        val separateLineSize:Int = (relation * noteSize).toInt()


        boardSize = 10 * separateLineSize + 9 * noteSize

        val test1 = resources.getDrawable(R.drawable.rook_0, context?.theme)

        val rel:Float = test1.intrinsicHeight.toFloat() / (noteSize * scaleNote)
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams((test1.intrinsicWidth / rel).toInt(),(test1.intrinsicHeight / rel).toInt())
        val top = (dm.heightPixels - boardSize) / 2
        val left = (dm.widthPixels - boardSize) / 2

        Log.i("Size", "Top: $top, Left: $left ")
        val topX = (displayWidth - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.width / 2
        val topY = (dm.heightPixels - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.height / 2

        Log.i("Size", "Width: ${dm.widthPixels}, Height: ${dm.heightPixels} ")
        binding.noteSize = noteSize
        binding.separateLineSize = separateLineSize
        binding.boardSize = boardSize

        val client = OkHttpClient()
        val request: Request = Request.Builder().url("ws://52.149.149.222:8080/chat").build()
        val webSocket = client.newWebSocket(request, SocketListener())


        shogi = activity?.let {
            ShogiModelMultiplayer(
                Orientation.NORMAL,topY.toFloat(),topX.toFloat(),noteSize, separateLineSize, layout,
                it,layoutParams,top.toFloat(),left.toFloat(),webSocket,Side.WHITE,Side.BLACK)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()
    }

    inner class SocketListener: WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.i("Moving:",text)
            var displacementInfo = gson.fromJson(text,DisplacementInfo::class.java)
            shogi?.movePieceAt(displacementInfo)

        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.i("Moving: "," Closed " +reason.toString())
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i("Moving ","Failure "+t.message)
        }
    }
}