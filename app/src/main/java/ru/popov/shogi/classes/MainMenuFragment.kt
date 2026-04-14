package ru.popov.shogi.classes

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import ru.popov.shogi.R
import kotlin.math.min

class MainMenuFragment : Fragment() {

    private var btnOneDevice: Button? = null
    private var btnOnlineGame: Button? = null
    private var logoFloatAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.main_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.menu_legacy_soften)?.visibility =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) View.GONE else View.VISIBLE

        view.findViewById<ImageView>(R.id.menu_background)?.let { bg ->
            bg.post {
                if (!bg.isAttachedToWindow) return@post
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    val minSide = when {
                        bg.width > 0 && bg.height > 0 -> min(bg.width, bg.height).toFloat()
                        else -> {
                            val dm = resources.displayMetrics
                            min(dm.widthPixels, dm.heightPixels).toFloat()
                        }
                    }
                    val radius = (minSide * 0.052f).coerceIn(10f, 20f)
                    bg.setRenderEffect(
                        RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.CLAMP)
                    )
                }
            }
        }

        val baseBottom = resources.getDimensionPixelSize(R.dimen.menu_bottom_margin)
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.findViewById<ConstraintLayout>(R.id.menu_content).updatePadding(bottom = baseBottom + bars.bottom)
            windowInsets
        }
        ViewCompat.requestApplyInsets(view)

        btnOneDevice = view.findViewById(R.id.one_device)
        btnOneDevice?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().also {
                it.replace(R.id.layout, TimeSelectFragment())
                it.addToBackStack(null)
                it.commit()
            }
        }

        btnOnlineGame = view.findViewById(R.id.online_game)
        btnOnlineGame?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().also {
                it.replace(R.id.layout, MultiplayerShogiFragment())
                it.commit()
            }
        }

        view.findViewById<ImageView>(R.id.menu_logo)?.let { logo ->
            logo.post {
                if (!logo.isAttachedToWindow) return@post
                playMainMenuIntro(view, savedInstanceState != null)
            }
        }
    }

    private fun playMainMenuIntro(container: View, skipIntro: Boolean) {
        val logo = container.findViewById<ImageView>(R.id.menu_logo) ?: return
        val title = container.findViewById<TextView>(R.id.menu_title) ?: return
        val actions = container.findViewById<LinearLayout>(R.id.menu_actions) ?: return

        logoFloatAnimator?.cancel()
        logoFloatAnimator = null
        logo.animate().cancel()
        title.animate().cancel()
        actions.animate().cancel()

        val d = resources.displayMetrics.density
        val slideLogo = 32f * d
        val slideTitle = 22f * d
        val slideActions = 28f * d

        if (skipIntro) {
            logo.alpha = 1f
            logo.scaleX = 1f
            logo.scaleY = 1f
            logo.translationY = 0f
            logo.pivotX = logo.width / 2f
            logo.pivotY = logo.height / 2f
            title.alpha = 1f
            title.translationY = 0f
            actions.alpha = 1f
            actions.translationY = 0f
            startLogoFloat(logo)
            return
        }

        logo.pivotX = logo.width / 2f
        logo.pivotY = logo.height / 2f

        logo.alpha = 0f
        logo.translationY = slideLogo
        logo.scaleX = 0.82f
        logo.scaleY = 0.82f

        title.alpha = 0f
        title.translationY = slideTitle

        actions.alpha = 0f
        actions.translationY = slideActions

        logo.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(560)
            .setInterpolator(OvershootInterpolator(0.88f))
            .withEndAction { startLogoFloat(logo) }
            .start()

        title.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(120)
            .setDuration(440)
            .setInterpolator(DecelerateInterpolator())
            .start()

        actions.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(260)
            .setDuration(420)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun startLogoFloat(logo: ImageView) {
        logoFloatAnimator?.cancel()
        val amp = 5.5f * resources.displayMetrics.density
        logoFloatAnimator = ObjectAnimator.ofFloat(logo, View.TRANSLATION_Y, 0f, -amp).apply {
            duration = 2200L
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
        }
        logoFloatAnimator?.start()
    }

    override fun onDestroyView() {
        logoFloatAnimator?.cancel()
        logoFloatAnimator = null
        view?.findViewById<ImageView>(R.id.menu_logo)?.animate()?.cancel()
        view?.findViewById<TextView>(R.id.menu_title)?.animate()?.cancel()
        view?.findViewById<LinearLayout>(R.id.menu_actions)?.animate()?.cancel()

        view?.findViewById<ImageView>(R.id.menu_background)?.let { bg ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                bg.setRenderEffect(null)
            }
        }
        super.onDestroyView()
    }
}
