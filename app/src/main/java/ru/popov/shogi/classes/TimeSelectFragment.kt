package ru.popov.shogi.classes

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ru.popov.shogi.R

class TimeSelectFragment : Fragment() {

    private lateinit var seekMinutes: AppCompatSeekBar
    private lateinit var seekIncrement: AppCompatSeekBar
    private lateinit var valueMinutes: TextView
    private lateinit var valueIncrement: TextView
    private lateinit var chipContainer: LinearLayout

    private val presetChips = mutableListOf<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_time_select, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seekMinutes = view.findViewById(R.id.seek_minutes)
        seekIncrement = view.findViewById(R.id.seek_increment)
        valueMinutes = view.findViewById(R.id.value_minutes)
        valueIncrement = view.findViewById(R.id.value_increment)
        chipContainer = view.findViewById(R.id.time_preset_chip_container)
        val btnPlay = view.findViewById<MaterialButton>(R.id.btn_play_time)

        seekMinutes.progress = 5
        seekIncrement.progress = 3
        syncValueLabels()

        val chipListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                syncValueLabels()
                if (fromUser) clearPresetChipSelection()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                syncPresetChipSelection()
            }
        }
        seekMinutes.setOnSeekBarChangeListener(chipListener)
        seekIncrement.setOnSeekBarChangeListener(chipListener)

        buildPresetChips()
        syncPresetChipSelection()

        btnPlay.setOnClickListener {
            openGame(seekMinutes.progress, seekIncrement.progress)
        }

        val baseBottom = resources.getDimensionPixelSize(R.dimen.time_select_content_bottom_base)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = baseBottom + bars.bottom)
            windowInsets
        }
        ViewCompat.requestApplyInsets(view)
    }

    private fun syncValueLabels() {
        valueMinutes.text = seekMinutes.progress.toString()
        valueIncrement.text = seekIncrement.progress.toString()
    }

    private fun buildPresetChips() {
        chipContainer.removeAllViews()
        presetChips.clear()
        val chipMarginEnd = resources.getDimensionPixelSize(R.dimen.time_chip_margin_end)
        val padH = (14 * resources.displayMetrics.density).toInt()
        val padV = (10 * resources.displayMetrics.density).toInt()
        val minH = (40 * resources.displayMetrics.density).toInt()

        TimePresets.numeric.forEach { preset ->
            val chip = TextView(requireContext()).apply {
                text = "${preset.minutes}+${preset.incrementSeconds}"
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                setTextColor(ContextCompat.getColorStateList(context, R.color.time_preset_chip_text))
                gravity = android.view.Gravity.CENTER
                setBackgroundResource(R.drawable.time_preset_chip_background)
                setPadding(padH, padV, padH, padV)
                minHeight = minH
                tag = preset
                setOnClickListener {
                    seekMinutes.progress = preset.minutes.coerceIn(0, seekMinutes.max)
                    seekIncrement.progress = preset.incrementSeconds.coerceIn(0, seekIncrement.max)
                    syncValueLabels()
                    setPresetChipSelected(this)
                }
            }
            chip.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { marginEnd = chipMarginEnd }
            chipContainer.addView(chip)
            presetChips += chip
        }

        val customChip = TextView(requireContext()).apply {
            text = getString(R.string.time_preset_custom)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            setTextColor(ContextCompat.getColor(context, R.color.time_tile_text_primary))
            gravity = android.view.Gravity.CENTER
            setBackgroundResource(R.drawable.time_preset_chip_custom_background)
            setPadding(padH, padV, padH, padV)
            minHeight = minH
            setOnClickListener {
                clearPresetChipSelection()
                showCustomDialog()
            }
        }
        customChip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        chipContainer.addView(customChip)
    }

    private fun setPresetChipSelected(selected: TextView) {
        presetChips.forEach { it.isSelected = it === selected }
    }

    private fun clearPresetChipSelection() {
        presetChips.forEach { it.isSelected = false }
    }

    private fun syncPresetChipSelection() {
        val m = seekMinutes.progress
        val i = seekIncrement.progress
        val match = presetChips.firstOrNull { chip ->
            val p = chip.tag as? TimePreset ?: return@firstOrNull false
            p.minutes == m && p.incrementSeconds == i
        }
        if (match != null) setPresetChipSelected(match) else clearPresetChipSelection()
    }

    private fun openGame(minutes: Int, incrementSeconds: Int) {
        val timed = minutes > 0 || incrementSeconds > 0
        val frag = ShogiFragment.newInstance(minutes, incrementSeconds, timed)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.layout, frag)
            .addToBackStack(null)
            .commit()
    }

    private fun showCustomDialog() {
        val ctx = requireContext()
        val dialogView = layoutInflater.inflate(R.layout.dialog_custom_time, null)
        val npMin = dialogView.findViewById<NumberPicker>(R.id.np_minutes)
        val npInc = dialogView.findViewById<NumberPicker>(R.id.np_increment)
        npMin.minValue = 0
        npMin.maxValue = 180
        npMin.value = seekMinutes.progress.coerceIn(0, 180)
        npInc.minValue = 0
        npInc.maxValue = 180
        npInc.value = seekIncrement.progress.coerceIn(0, 180)
        MaterialAlertDialogBuilder(ctx)
            .setTitle(R.string.custom_time_dialog_title)
            .setView(dialogView)
            .setPositiveButton(R.string.play) { _, _ ->
                applyFromPickers(npMin.value, npInc.value)
                openGame(npMin.value, npInc.value)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun applyFromPickers(minutes: Int, incrementSeconds: Int) {
        if (minutes > seekMinutes.max) seekMinutes.max = minutes
        if (incrementSeconds > seekIncrement.max) seekIncrement.max = incrementSeconds
        seekMinutes.progress = minutes.coerceIn(0, seekMinutes.max)
        seekIncrement.progress = incrementSeconds.coerceIn(0, seekIncrement.max)
        syncValueLabels()
        syncPresetChipSelection()
    }
}
