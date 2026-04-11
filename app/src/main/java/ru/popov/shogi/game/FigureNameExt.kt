package ru.popov.shogi.game

import ru.popov.shogi.classes.figures.FigureName

/** В руке фигура хранится в демотированном виде; привязка к [FigureName] после взятия. */
fun FigureName.normalizedForHand(): FigureName = when (this) {
    FigureName.TOKIN -> FigureName.PAWN
    FigureName.PROMOTED_SILVER -> FigureName.SILVER
    FigureName.PROMOTED_LANCE -> FigureName.LANCE
    FigureName.PROMOTED_KNIGHT -> FigureName.KNIGHT
    FigureName.PROMOTED_ROOK -> FigureName.ROOK
    FigureName.PROMOTED_BISHOP -> FigureName.BISHOP
    else -> this
}
