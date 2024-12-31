package com.flow.tetris.core

import com.flow.tetris.R

enum class MinoType {
    I,
    O,
    S,
    Z,
    J,
    L,
    T;

    val block: BlockType
        get() {
            return when (this) {
                I -> BlockType.I
                O -> BlockType.O
                S -> BlockType.S
                Z -> BlockType.Z
                J -> BlockType.J
                L -> BlockType.L
                T -> BlockType.T
            }
        }

    val colorId: Int
        get() {
            return when (this) {
                I -> R.color.i_mino
                O -> R.color.o_mino
                S -> R.color.s_mino
                Z -> R.color.z_mino
                J ->  R.color.j_mino
                L -> R.color.l_mino
                T -> R.color.t_mino
            }
        }

    val firstPosition: List<Int>
        get() {
            return when (this) {
                I -> listOf(3, 4, 5, 6)
                O -> listOf(4, 5, 14, 15)
                S -> listOf(4, 5, 13, 14)
                Z -> listOf(3, 4, 14, 15)
                J -> listOf(3, 13, 14, 15)
                L -> listOf(5, 13, 14, 15)
                T -> listOf(4, 13, 14, 15)
            }
        }
}

enum class BlockType(rawValue: Int) {
    NONE(0),
    I(1),
    O(2),
    S(3),
    Z(4),
    J(5),
    L(6),
    T(7),
    GHOST(8),
    FALL(9);

    val isEmpty: Boolean
        get() = this == NONE

    val colorId: Int
        get() {
            return when (this) {
                NONE, GHOST, FALL -> R.color.white
                I -> R.color.i_mino
                O -> R.color.o_mino
                S -> R.color.s_mino
                Z -> R.color.z_mino
                J -> R.color.j_mino
                L -> R.color.l_mino
                T -> R.color.t_mino
            }
        }

    companion object {
        fun ordinalOf(position: Int): BlockType {
            val p = if (position in 0..entries.size) position else NONE.ordinal
            return entries.first { it.ordinal == p }
        }
    }
}

enum class RotateType {
    FIRST,
    SECOND,
    THIRD,
    FOURTH;

    val nextType: RotateType
        get() {
            return when (this) {
                FIRST -> SECOND
                SECOND -> THIRD
                THIRD -> FOURTH
                FOURTH -> FIRST
            }
        }
}

enum class PointType {
    SINGLE,
    DOUBLE,
    TRIPLE,
    TETRIS;

    val point: Int
        get() {
            return when (this) {
                SINGLE -> 10
                DOUBLE -> 30
                TRIPLE -> 50
                TETRIS -> 80
            }
        }
}