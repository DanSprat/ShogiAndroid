package ru.popov.shogi.classes

import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import ru.popov.shogi.MainActivity
import ru.popov.shogi.R
import ru.popov.shogi.databinding.FragmentShogiBinding


class ShogiFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val relation:Float = 0.1f
        val dm = resources.displayMetrics
        val displayWidth = dm.widthPixels

        var paddingX:Int = ((displayWidth*0.1).toInt() / 2)
        var boardSize:Int = displayWidth - paddingX
        val noteSize:Int = (boardSize / (10 * relation + 9)).toInt()
        val separateLineSize:Int = (relation * noteSize).toInt()

        val binding: FragmentShogiBinding = FragmentShogiBinding.inflate(inflater,container,false)
        binding.noteSize = noteSize
        binding.separateLineSize = separateLineSize

        return binding.root
    }
    
}