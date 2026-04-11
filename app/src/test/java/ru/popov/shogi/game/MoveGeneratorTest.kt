package ru.popov.shogi.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.popov.shogi.classes.ShogiRules
import ru.popov.shogi.classes.figures.FigureName
import ru.popov.shogi.classes.figures.Side

class MoveGeneratorTest {

    private fun emptyBoard(): BoardSnapshot = BoardSnapshot.empty()

    private fun boardWith(vararg placed: Triple<Int, Int, BoardCell>): BoardSnapshot {
        val cells = Array(9) { arrayOfNulls<BoardCell?>(9) }
        for ((col, row, cell) in placed) {
            cells[col - 1][row - 1] = cell
        }
        return BoardSnapshot.fromCells(cells)
    }

    @Test
    fun whitePawn_movesOneStepForward() {
        val board = boardWith(Triple(5, 5, BoardCell(Side.WHITE, FigureName.PAWN, false)))
        val moves = MoveGenerator.legalMoves(
            board, 5, 5, false, Side.WHITE, FigureName.PAWN, ShogiRules.PAWN_WHITE
        )
        assertEquals(setOf(5 to 6), moves)
    }

    @Test
    fun whitePawn_blockedByOwnPiece() {
        val board = boardWith(
            Triple(5, 5, BoardCell(Side.WHITE, FigureName.PAWN, false)),
            Triple(5, 6, BoardCell(Side.WHITE, FigureName.GOLD, false))
        )
        val moves = MoveGenerator.legalMoves(
            board, 5, 5, false, Side.WHITE, FigureName.PAWN, ShogiRules.PAWN_WHITE
        )
        assertTrue(moves.isEmpty())
    }

    @Test
    fun whitePawn_canCaptureEnemy() {
        val board = boardWith(
            Triple(5, 5, BoardCell(Side.WHITE, FigureName.PAWN, false)),
            Triple(5, 6, BoardCell(Side.BLACK, FigureName.PAWN, false))
        )
        val moves = MoveGenerator.legalMoves(
            board, 5, 5, false, Side.WHITE, FigureName.PAWN, ShogiRules.PAWN_WHITE
        )
        assertEquals(setOf(5 to 6), moves)
    }

    @Test
    fun pawnDrop_excludesLastRankForWhite() {
        val board = emptyBoard()
        val moves = MoveGenerator.legalMoves(
            board, 0, 0, true, Side.WHITE, FigureName.PAWN, ShogiRules.PAWN_WHITE
        )
        assertFalse(moves.any { (_, row) -> row == 9 })
    }

    @Test
    fun pawnDrop_excludesFirstRankForBlack() {
        val board = emptyBoard()
        val moves = MoveGenerator.legalMoves(
            board, 0, 0, true, Side.BLACK, FigureName.PAWN, ShogiRules.PAWN_BLACK
        )
        assertFalse(moves.any { (_, row) -> row == 1 })
    }

    @Test
    fun pawnDrop_notOnFileWithFriendlyUnpromotedPawn() {
        val board = boardWith(Triple(3, 5, BoardCell(Side.WHITE, FigureName.PAWN, false)))
        val moves = MoveGenerator.legalMoves(
            board, 0, 0, true, Side.WHITE, FigureName.PAWN, ShogiRules.PAWN_WHITE
        )
        assertFalse(moves.any { (col, _) -> col == 3 })
    }

    @Test
    fun tokinNormalizesToPawnForDrops() {
        assertEquals(FigureName.PAWN, FigureName.TOKIN.normalizedForHand())
    }
}
