package ru.popov.shogi.classes

import ru.popov.shogi.R

enum class BundlePiece(var normalId:Int,var reverseId:Int) {

    PAWN(R.drawable.pawn_0,R.drawable.pawn_1),
    LANCE(R.drawable.lance_0,R.drawable.lance_1),
    KNIGHT(R.drawable.knight_0,R.drawable.knight_1),
    GOLD(R.drawable.gold_0,R.drawable.gold_1),
    SILVER(R.drawable.silver_0,R.drawable.silver_1),
    ROOK(R.drawable.rook_0,R.drawable.rook_1),
    BISHOP(R.drawable.bishop_0,R.drawable.bishop_1)
}