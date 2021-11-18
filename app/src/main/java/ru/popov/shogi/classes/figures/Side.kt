package ru.popov.shogi.classes.figures

enum class Side {
    BLACK {
        override fun next(): Side {
            return WHITE
        }
    },
    WHITE {
        override fun next(): Side {
            return BLACK
        }
    };

    abstract fun next(): Side
}