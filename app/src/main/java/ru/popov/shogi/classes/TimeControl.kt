package ru.popov.shogi.classes

/**
 * Контроль времени: начальное время на партию + прибавка Фишера за ход (в миллисекундах).
 */
data class TimeControl(
    val initialMillis: Long,
    val incrementMillis: Long,
    val enabled: Boolean
)
