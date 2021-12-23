package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import ru.popov.shogi.MainActivity
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.Orientation
import ru.popov.shogi.classes.figures.Side
import ru.popov.shogi.databinding.FragmentShogiBinding


class ShogiFragment : Fragment() {

    private lateinit var inflater:LayoutInflater
    private lateinit var shogi:ShogiModel
    private final val bitmaps = HashMap<Int, Bitmap> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val scaleNote:Float = 0.9f
        val relation:Float = 0.1f
        val binding: FragmentShogiBinding = FragmentShogiBinding.inflate(inflater,container,false)

        val layout:RelativeLayout = binding.root.findViewById(R.id.layout_SHG)

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
        val topX = (displayWidth - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.width / 2
        val topY = (dm.heightPixels - boardSize) / 2 + separateLineSize + noteSize / 2 - layoutParams.height / 2

        binding.noteSize = noteSize
        binding.separateLineSize = separateLineSize
        binding.boardSize = boardSize


        val game = activity?.let {
            ShogiModel(Orientation.NORMAL,topY.toFloat(),topX.toFloat(),noteSize, separateLineSize, layout,
                it,layoutParams
            )
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
}