package ru.popov.shogi.classes

enum class ShogiRules(private val rules: Array<VectorMove>) {
    PAWN_WHITE(arrayOf(VectorMove(Pair(0, 1)))) {
        override fun next() = PAWN_BLACK
    },

    PAWN_BLACK(arrayOf(VectorMove(Pair(0, -1)))) {
        override fun next() = PAWN_WHITE
    },

    KING(
        arrayOf(
            VectorMove(Pair(-1, 1)),
            VectorMove(Pair(-1, -1)),
            VectorMove(Pair(1, -1)),
            VectorMove(Pair(1, 1)),
            VectorMove(Pair(0, -1)),
            VectorMove(Pair(0, 1)),
            VectorMove(Pair(1, 0)),
            VectorMove(Pair(-1, 0))
        )
    ) {
        override fun next() = this
    },

    GOLD_WHITE(
        arrayOf(
            VectorMove(Pair(1, 1)),
            VectorMove(Pair(-1, 1)),
            VectorMove(Pair(0, -1)),
            VectorMove(Pair(0, 1)),
            VectorMove(Pair(-1, 0)),
            VectorMove(Pair(1, 0))
        )
    ) {
        override fun next() = GOLD_BLACK
    },

    GOLD_BLACK(
        arrayOf(
            VectorMove(Pair(1, -1)),
            VectorMove(Pair(-1, -1)),
            VectorMove(Pair(0, -1)),
            VectorMove(Pair(0, 1)),
            VectorMove(Pair(-1, 0)),
            VectorMove(Pair(1, 0))
        )
    ) {
        override fun next() = GOLD_WHITE
    },

    SILVER_BLACK(
        arrayOf(
            VectorMove(Pair(1, -1)),
            VectorMove(Pair(-1, -1)),
            VectorMove(Pair(0, -1)),
            VectorMove(Pair(1, 1)),
            VectorMove(Pair(-1, 1))
        )
    ) {
        override fun next() = SILVER_WHITE
    },

    SILVER_WHITE(
        arrayOf(
            VectorMove(Pair(1, 1)),
            VectorMove(Pair(-1, 1)),
            VectorMove(Pair(0, 1)),
            VectorMove(Pair(-1, -1)),
            VectorMove(Pair(1, -1))
        )
    ) {
        override fun next() = SILVER_BLACK
    },

    ROOK(
        arrayOf(
            VectorMove(Pair(1, 0), 8),
            VectorMove(Pair(0, 1), 8),
            VectorMove(Pair(-1, 0), 8),
            VectorMove(Pair(0, -1), 8)
        )
    ) {
        override fun next() = this
    },

    LANCE_WHITE(
        arrayOf(
            VectorMove(Pair(0, 1), 8)
        )
    ) {
        override fun next() = LANCE_BLACK
    },

    LANCE_BLACK(
        arrayOf(
            VectorMove(Pair(0, -1), 8)
        )
    ) {
        override fun next() = LANCE_WHITE
    },

    BISHOP(
        arrayOf(
            VectorMove(Pair(1, 1), 8),
            VectorMove(Pair(1, -1), 8),
            VectorMove(Pair(-1, 1), 8),
            VectorMove(Pair(-1, -1), 8),
        )
    ) {
        override fun next() = this
    },

    KNIGHT_WHITE(
        arrayOf(
            VectorMove(Pair(1, 2)),
            VectorMove(Pair(-1, 2)),
        )
    ) {
        override fun next() = KNIGHT_BLACK
    },

    KNIGHT_BLACK(
        arrayOf(
            VectorMove(Pair(1, -2)),
            VectorMove(Pair(-1, -2)),
        )
    ) {
        override fun next() = KNIGHT_WHITE
    },

    DRAGON(
        arrayOf(
            VectorMove(Pair(0, 1), 8),
            VectorMove(Pair(1, 0), 8),
            VectorMove(Pair(0, -1), 8),
            VectorMove(Pair(-1, 0), 8),
            VectorMove(Pair(-1, 1)),
            VectorMove(Pair(1, -1)),
            VectorMove(Pair(-1, -1)),
            VectorMove(Pair(1, 1)),
        )
    ) {
        override fun next() = this
    },
    HORSE(arrayOf(
        VectorMove(Pair(1, 1), 8),
        VectorMove(Pair(-1, -1), 8),
        VectorMove(Pair(1, -1), 8),
        VectorMove(Pair(-1, 1), 8),
        VectorMove(Pair(1, 0)),
        VectorMove(Pair(-1, 0)),
        VectorMove(Pair(0, 1)),
        VectorMove(Pair(0, -1)),
    )) {
        override fun next() = this
    };


    val size: Int
        get() = rules.size

    operator fun get(i: Int) = rules[i]
    abstract fun next(): ShogiRules

}