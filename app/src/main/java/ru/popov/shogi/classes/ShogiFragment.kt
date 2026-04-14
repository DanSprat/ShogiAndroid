package ru.popov.shogi.classes

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import ru.popov.shogi.R
import ru.popov.shogi.classes.figures.Orientation
import ru.popov.shogi.classes.figures.Side
import ru.popov.shogi.databinding.FragmentShogiBinding

class ShogiFragment : Fragment() {

    private var _binding: FragmentShogiBinding? = null
    private val binding get() = _binding!!

    private lateinit var inflater: LayoutInflater
    private lateinit var shogi: ShogiModel

    companion object {
        private const val ARG_MINUTES = "minutes"
        private const val ARG_INCREMENT = "incrementSeconds"
        private const val ARG_TIMED = "timed"

        fun newInstance(minutes: Int, incrementSeconds: Int, timed: Boolean) = ShogiFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MINUTES, minutes)
                putInt(ARG_INCREMENT, incrementSeconds)
                putBoolean(ARG_TIMED, timed)
            }
        }
    }

    private val bitmaps = HashMap<Int, Bitmap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("ResourceType", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShogiBinding.inflate(inflater, container, false)
        this.inflater = inflater

        val bl = ShogiBoardLayoutHelper.compute(resources, context?.theme)
        Log.i("Size", "Top: ${bl.top}, Left: ${bl.left} ")
        Log.i("Size", "Width: ${resources.displayMetrics.widthPixels}, Height: ${resources.displayMetrics.heightPixels} ")
        binding.noteSize = bl.noteSize
        binding.separateLineSize = bl.separateLineSize
        binding.boardSize = bl.boardSize

        val minutes = arguments?.getInt(ARG_MINUTES, 10) ?: 10
        val incrementSec = arguments?.getInt(ARG_INCREMENT, 0) ?: 0
        val timed = arguments?.getBoolean(ARG_TIMED, true) ?: true
        val timeControl = if (timed) {
            TimeControl(
                initialMillis = minutes * 60_000L,
                incrementMillis = incrementSec * 1000L,
                enabled = true
            )
        } else {
            TimeControl(0L, 0L, enabled = false)
        }

        val layout: RelativeLayout = binding.root.findViewById(R.id.layout_SHG)
        val clockBlack = binding.root.findViewById<TextView>(R.id.clock_black)
        val clockWhite = binding.root.findViewById<TextView>(R.id.clock_white)

        val onGameOver: (Side, Boolean) -> Unit = { losing, byKing ->
            showGameOverOverlay(losing, byKing)
        }

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
            bl.left.toFloat(),
            timeControl,
            clockBlack,
            clockWhite,
            onGameOver
        )

        return binding.root
    }

    override fun onDestroyView() {
        setBoardBlur(false)
        if (::shogi.isInitialized) {
            shogi.stopGameClock()
        }
        _binding = null
        super.onDestroyView()
    }

    private fun setBoardBlur(enable: Boolean) {
        val board = _binding?.root?.findViewById<RelativeLayout>(R.id.layout_SHG) ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            board.setRenderEffect(
                if (enable) {
                    RenderEffect.createBlurEffect(22f, 22f, Shader.TileMode.CLAMP)
                } else {
                    null
                }
            )
        }
    }

    private fun showGameOverOverlay(losing: Side, byKingCapture: Boolean = false) {
        val root = _binding?.root ?: return
        val overlay = root.findViewById<FrameLayout>(R.id.game_over_overlay) ?: return
        val loserName = when (losing) {
            Side.WHITE -> getString(R.string.side_white)
            Side.BLACK -> getString(R.string.side_black)
        }
        root.findViewById<TextView>(R.id.game_over_title).text =
            if (byKingCapture) getString(R.string.game_over_title_king)
            else getString(R.string.game_over_title_time)
        root.findViewById<TextView>(R.id.game_over_message).text =
            if (byKingCapture) getString(R.string.game_over_message_king, loserName)
            else getString(R.string.game_over_message_time, loserName)
        setBoardBlur(true)
        overlay.visibility = View.VISIBLE
        overlay.findViewById<MaterialButton>(R.id.game_over_restart).setOnClickListener {
            restartGame()
        }
        overlay.findViewById<MaterialButton>(R.id.game_over_main_menu).setOnClickListener {
            goToMainMenu()
        }
    }

    private fun restartGame() {
        val m = arguments?.getInt(ARG_MINUTES, 10) ?: 10
        val inc = arguments?.getInt(ARG_INCREMENT, 0) ?: 0
        val timed = arguments?.getBoolean(ARG_TIMED, true) ?: true
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.layout, newInstance(m, inc, timed))
            .addToBackStack(null)
            .commit()
    }

    private fun goToMainMenu() {
        val fm = requireActivity().supportFragmentManager
        while (fm.backStackEntryCount > 0) {
            fm.popBackStackImmediate()
        }
        fm.beginTransaction()
            .replace(R.id.layout, MainMenuFragment())
            .commit()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bl = ShogiBoardLayoutHelper.compute(resources, context?.theme)
        applyClockMarginsInBoardMargins(view, bl)
    }

    /** Вертикально по центру полосы над/под доской (тёмное пространство до края экрана). */
    private fun applyClockMarginsInBoardMargins(root: View, bl: ShogiBoardLayout) {
        val layout = root.findViewById<RelativeLayout>(R.id.layout_SHG) ?: return
        val clockBlack = root.findViewById<TextView>(R.id.clock_black) ?: return
        val clockWhite = root.findViewById<TextView>(R.id.clock_white) ?: return
        val minPad = (4 * resources.displayMetrics.density).toInt()
        val H = resources.displayMetrics.heightPixels
        val boardTop = bl.top
        val boardBottom = bl.top + bl.boardSize

        layout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                layout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val hTop = clockBlack.height
                val topBand = boardTop
                val marginTopPx = if (topBand > 0 && hTop > 0) {
                    (topBand / 2 - hTop / 2).coerceAtLeast(minPad)
                } else minPad
                (clockBlack.layoutParams as RelativeLayout.LayoutParams).apply {
                    topMargin = marginTopPx
                }
                clockBlack.requestLayout()

                val hBot = clockWhite.height
                val bottomBand = H - boardBottom
                val marginBottomPx = if (bottomBand > 0 && hBot > 0) {
                    (bottomBand / 2 - hBot / 2).coerceAtLeast(minPad)
                } else minPad
                (clockWhite.layoutParams as RelativeLayout.LayoutParams).apply {
                    bottomMargin = marginBottomPx
                }
                clockWhite.requestLayout()
            }
        })
    }

    @SuppressLint("ResourceType")
    override fun onStart() {
        super.onStart()
    }
}
