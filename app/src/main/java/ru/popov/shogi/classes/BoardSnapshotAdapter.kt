package ru.popov.shogi.classes

import ru.popov.shogi.classes.figures.Figure
import ru.popov.shogi.game.BoardCell
import ru.popov.shogi.game.BoardSnapshot
import ru.popov.shogi.game.MoveGenerator

fun BoardArray.toSnapshot(): BoardSnapshot {
    val cells = Array(9) { arrayOfNulls<BoardCell?>(9) }
    for (col in 1..9) {
        for (row in 1..9) {
            this[col, row]?.let { fig ->
                cells[col - 1][row - 1] = BoardCell(fig.side, fig.name, fig.promoted)
            }
        }
    }
    return BoardSnapshot.fromCells(cells)
}

/** Единая точка расчёта ходов для [ShogiModel] и [ShogiModelMultiplayer]. */
fun Figure.computeLegalMoveTargets(board: BoardArray): HashSet<Pair<Int, Int>> =
    MoveGenerator.legalMoves(
        board.toSnapshot(),
        col,
        row,
        eaten,
        side,
        name,
        rules
    )
