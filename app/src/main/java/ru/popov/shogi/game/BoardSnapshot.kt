package ru.popov.shogi.game

/**
 * Доска 9×9. Индексы [col][row] в диапазоне 1..9, как в [ru.popov.shogi.classes.BoardArray].
 */
class BoardSnapshot private constructor(private val cells: Array<Array<BoardCell?>>) {

    operator fun get(col: Int, row: Int): BoardCell? =
        if (col in 1..9 && row in 1..9) cells[col - 1][row - 1] else null

    companion object {
        fun empty(): BoardSnapshot = BoardSnapshot(Array(9) { arrayOfNulls(9) })

        fun fromCells(cells: Array<Array<BoardCell?>>): BoardSnapshot {
            val copy = Array(9) { c -> Array<BoardCell?>(9) { r -> cells[c][r] } }
            return BoardSnapshot(copy)
        }
    }
}
