package ru.popov.shogi.game

import ru.popov.shogi.classes.figures.FigureName
import ru.popov.shogi.classes.figures.Side

data class BoardCell(
    val side: Side,
    val name: FigureName,
    val promoted: Boolean
)
