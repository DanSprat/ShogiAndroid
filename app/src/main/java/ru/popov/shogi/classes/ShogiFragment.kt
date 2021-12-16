package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
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
import ru.popov.shogi.classes.figures.Side
import ru.popov.shogi.databinding.FragmentShogiBinding


class ShogiFragment : Fragment() {

    private lateinit var inflater:LayoutInflater
    private lateinit var shogi:ShogiModel
    private final val bitmaps = HashMap<Int, Bitmap> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val relation:Float = 0.1f
        val dm = resources.displayMetrics
        val displayWidth = dm.widthPixels
        this.inflater = inflater
        var paddingX:Int = ((displayWidth*0.1).toInt() / 2)
        var boardSize:Int = displayWidth - paddingX
        val noteSize:Int = (boardSize / (10 * relation + 9)).toInt()
        val separateLineSize:Int = (relation * noteSize).toInt()
        shogi = ShogiModel()

        // val rootView: View = inflater.inflate(R.layout.fragment_shogi,null,false)



        val binding: FragmentShogiBinding = FragmentShogiBinding.inflate(inflater,container,false)
        binding.noteSize = noteSize
        binding.separateLineSize = separateLineSize

        val layout:RelativeLayout = binding.root.findViewById(R.id.layout_SHG)
        val game = ShogiModel()
        context?.let { game.connect(it,layout,100f,100f,null,null,noteSize,Side.WHITE,null) }
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