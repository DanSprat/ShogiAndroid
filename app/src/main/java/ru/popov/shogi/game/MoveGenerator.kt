package ru.popov.shogi.game

import ru.popov.shogi.classes.ShogiRules
import ru.popov.shogi.classes.figures.FigureName
import ru.popov.shogi.classes.figures.Side

/**
 * Расчёт допустимых клеток (col, row) по правилам, совместимым с прежним [searchRoots].
 */
object MoveGenerator {

    fun legalMoves(
        board: BoardSnapshot,
        col: Int,
        row: Int,
        inHand: Boolean,
        side: Side,
        pieceName: FigureName,
        rules: ShogiRules
    ): HashSet<Pair<Int, Int>> =
        if (inHand) drops(board, side, pieceName.normalizedForHand())
        else movesFromBoard(board, col, row, side, rules)

    private fun fileHasUnpromotedPawn(board: BoardSnapshot, file: Int, side: Side): Boolean {
        for (r in 1..9) {
            val c = board[file, r] ?: continue
            if (c.side == side && c.name == FigureName.PAWN && !c.promoted) return true
        }
        return false
    }

    private fun drops(board: BoardSnapshot, side: Side, name: FigureName): HashSet<Pair<Int, Int>> {
        val moves = HashSet<Pair<Int, Int>>()
        when (name) {
            FigureName.PAWN -> {
                for (file in 1..9) {
                    if (fileHasUnpromotedPawn(board, file, side)) continue
                    for (r in 1..9) {
                        if (side == Side.BLACK && r == 1) continue
                        if (side == Side.WHITE && r == 9) continue
                        if (board[file, r] == null) moves.add(file to r)
                    }
                }
            }
            FigureName.KNIGHT -> {
                val rows = if (side == Side.WHITE) 1..7 else 3..9
                for (file in 1..9) for (r in rows) if (board[file, r] == null) moves.add(file to r)
            }
            FigureName.LANCE -> {
                val rows = if (side == Side.WHITE) 1..8 else 2..9
                for (file in 1..9) for (r in rows) if (board[file, r] == null) moves.add(file to r)
            }
            else -> {
                for (file in 1..9) for (r in 1..9) if (board[file, r] == null) moves.add(file to r)
            }
        }
        return moves
    }

    private fun movesFromBoard(
        board: BoardSnapshot,
        col: Int,
        row: Int,
        side: Side,
        rules: ShogiRules
    ): HashSet<Pair<Int, Int>> {
        val moves = HashSet<Pair<Int, Int>>()
        var cx = col
        var cy = row
        for (i in 0 until rules.size) {
            val vectorMove = rules[i]
            val x = vectorMove.vector.first
            val y = vectorMove.vector.second
            var isEat = false
            for (step in 0 until vectorMove.length) {
                val nx = cx + x
                val ny = cy + y
                if (nx < 1 || nx > 9 || ny < 1 || ny > 9) break
                if (board[nx, ny]?.side == side || isEat) break
                board[nx, ny]?.let { occ ->
                    if (occ.side != side) isEat = true
                }
                cx = nx
                cy = ny
                moves.add(cx to cy)
            }
            cx = col
            cy = row
        }
        return moves
    }
}
