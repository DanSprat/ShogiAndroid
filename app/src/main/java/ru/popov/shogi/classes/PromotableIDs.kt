package ru.popov.shogi.classes

import ru.popov.shogi.R

enum class PromotableIDs(var commonUP:Int,var commonDown:Int,var promotedUP:Int,var promotedDown:Int) {
    LANCE(R.drawable.lance_0,R.drawable.lance_1,R.drawable.p_lance_0,R.drawable.p_lance_1),
    KNIGHT(R.drawable.knight_0,R.drawable.knight_1,R.drawable.p_knight_0,R.drawable.p_knight_1),
    SILVER(R.drawable.silver_0,R.drawable.silver_1,R.drawable.p_silver_0,R.drawable.p_silver_1),
    PAWN(R.drawable.pawn_0,R.drawable.pawn_1,R.drawable.tokin_0,R.drawable.tokin_1),
    ROOK(R.drawable.rook_0,R.drawable.rook_1,R.drawable.p_rook_0,R.drawable.p_rook_1),
    BISHOP(R.drawable.bishop_0,R.drawable.bishop_1,R.drawable.p_bishop_0,R.drawable.p_bishop_1),
}