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
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.Orientation
import ru.popov.shogi.databinding.FragmentShogiBinding

class ShogiFragment : Fragment() {

    private lateinit var inflater: LayoutInflater
    private lateinit var shogi: ShogiModel

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

        shogi = ShogiModel(
            Orientation.NORMAL,
            bl.topY.toFloat(),
            bl.topX.toFloat(),
            bl.noteSize,
            bl.separateLineSize,
            layout,
            requireActivity(),
            bl.layoutParams,
            bl.top.toFloat(),
            bl.left.toFloat()
        )

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
