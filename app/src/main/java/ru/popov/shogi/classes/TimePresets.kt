package ru.popov.shogi.classes

data class TimePreset(
    val minutes: Int,
    val incrementSeconds: Int,
    val subtitle: String,
    val titleOverride: String? = null,
    val isCustom: Boolean = false
) {
    val titleLine: String
        get() = titleOverride ?: "$minutes + $incrementSeconds"
}

object TimePresets {
    val all: List<TimePreset> = listOf(
        TimePreset(1, 0, "Пуля"),
        TimePreset(2, 1, "Пуля"),
        TimePreset(3, 0, "Блиц"),
        TimePreset(3, 2, "Блиц"),
        TimePreset(5, 0, "Блиц"),
        TimePreset(5, 3, "Блиц"),
        TimePreset(10, 0, "Рапид"),
        TimePreset(10, 5, "Рапид"),
        TimePreset(15, 10, "Рапид"),
        TimePreset(30, 0, "Классика"),
        TimePreset(30, 20, "Классика"),
        TimePreset(0, 0, "", titleOverride = "Своя игра", isCustom = true)
    )

    val numeric: List<TimePreset> get() = all.filter { !it.isCustom }
}
