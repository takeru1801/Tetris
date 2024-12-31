package com.flow.tetris.core

import android.util.Log
import java.util.Random

var currentType = MinoType.I
var rotateType = RotateType.FIRST // 0 -> 1 -> 2 -> 3 -> 0
var tetrisList = Array(20) { Array(10) { 0 } }
var fallTime = 0.5f
var point = 0

object Tetris {

    const val ROW = 20
    const val COL = 10
    const val SIZE = ROW * COL

    val POSITION_RANGE = 0 until SIZE
    val LAST_ROW_RANGE = 190 until SIZE

    // Tetris Common Properties

    fun resetTetrisList() {
        tetrisList = Array(20) { Array(10) { 0 } }
    }

    fun getCloneTetrisList(): Array<Array<Int>> {
        val tempList = Array(20) { Array(10) { 0 } }
        for (i in 0 until SIZE) {
            tempList[i/COL][i%COL] = tetrisList[i/COL][i%COL]
        }
        return tempList
    }

    fun getNextMinos(): List<MinoType> {
        val minoList = MinoType.entries.toMutableList()
        minoList.shuffle(Random())
        return minoList
    }

    fun getFallPositions(): List<Int> {
        val list = mutableListOf<Int>()
        for (i in 0 until SIZE) {
            if (tetrisList[i/ COL][i% COL] == BlockType.FALL.ordinal) {
                list.add(i)
            }
        }
        return list
    }

    fun isReachLeft(): Boolean {
        val fallPosition = getFallPositions()
        fallPosition.forEach {
            if (it%COL == 0) return true
            // 1マス左が空白かどうか
            if (tetrisList[it/COL][it%COL - 1] != BlockType.NONE.ordinal && tetrisList[it/COL][it%COL - 1] != BlockType.FALL.ordinal) {
                return true
            }
        }

        return false
    }

    fun isReachRight(): Boolean {
        val fallPosition = getFallPositions()
        fallPosition.forEach {
            if (it%COL == 9) return true
            // 1マス左が空白かどうか
            if (tetrisList[it/COL][it%COL + 1] != BlockType.NONE.ordinal && tetrisList[it/COL][it%COL + 1] != BlockType.FALL.ordinal) {
                return true
            }
        }
        return false
    }

    fun isReachBottom(): Boolean {
        val fallPosition = getFallPositions()
        fallPosition.forEach {
            if (LAST_ROW_RANGE.contains(it)) return true
            // 1マス下が空白かどうか
            if (tetrisList[(it / COL) + 1][it% COL] != BlockType.NONE.ordinal && tetrisList[(it / COL) + 1][it% COL] != BlockType.FALL.ordinal) {
                return true
            }
        }
        return false
    }

    fun getUpdatePositions(list: Array<Array<Int>>): List<Int> {
        val updatePositions = mutableListOf<Int>()
        for (i in 0 until SIZE) {
            if (list[i/COL][i%COL] != tetrisList[i/COL][i%COL]) {
                updatePositions.add(i)
            }
        }
        return updatePositions
    }

    /**
     *
     * @param rotatePositions : 回転後のポジション
     * @param minoType : 現在のミノタイプ
     * @param rotateType : 現在のRotateType
     */
    fun isRotate(rotatePositions: List<Int>, minoType: MinoType, rotateType: RotateType): Boolean {
        // positionが枠内に入らない
        val currentPositions = getFallPositions()
        rotatePositions.forEach {
            if (it !in POSITION_RANGE) return false

        }

        // BlockType != NONEのとき
        rotatePositions.forEach {
            if (tetrisList[it/COL][it%COL] != BlockType.NONE.ordinal && tetrisList[it/COL][it%COL] != BlockType.FALL.ordinal) {
                return false
            }
        }

        when (minoType) {
            MinoType.I -> {
                when (rotateType) {
                    RotateType.FIRST -> {}
                    RotateType.SECOND -> {
                        currentPositions.forEach {
                            if (it% COL <= 1 || it% COL == 9) return false
                        }
                    }
                    RotateType.THIRD -> {}
                    RotateType.FOURTH -> {
                        currentPositions.forEach {
                            if (it%COL == 0 || it%COL >= 8) return false
                        }
                    }
                }
            }
            MinoType.O -> {}
            MinoType.S, MinoType.Z, MinoType.J, MinoType.L, MinoType.T -> {
                when (rotateType) {
                    RotateType.FIRST -> {}
                    RotateType.SECOND -> {
                        currentPositions.forEach {
                            if (it%COL == 0) return false
                        }
                    }
                    RotateType.THIRD -> {}
                    RotateType.FOURTH -> {
                        currentPositions.forEach {
                            if (it%COL == 9) return false
                        }
                    }
                }
            }
        }
        return true
    }

    fun getRotatePositions(type: MinoType): List<Int> {
        val positions = mutableListOf<Int>()
        val fallPositions = getFallPositions().toMutableList()
        fallPositions.sort()
        when (type) {
            MinoType.I -> {
                when (rotateType) {
                    RotateType.FIRST -> {
                        // index == 2を基準にする
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), baseIndex, (baseIndex+10), (baseIndex+20)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-2), (baseIndex-1), baseIndex, (baseIndex+1)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-20), (baseIndex-10), baseIndex, (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-1), baseIndex, (baseIndex+1), (baseIndex+2)).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
            MinoType.O -> {}
            MinoType.S -> {
                when (rotateType) {
                    RotateType.FIRST -> {
                        val baseIndex = fallPositions[3]
                        listOf((baseIndex-10), baseIndex, (baseIndex+1), (baseIndex+11)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[1]
                        listOf(baseIndex, (baseIndex+1), (baseIndex+9), (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[0]
                        listOf((baseIndex-11), (baseIndex-1), baseIndex, (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), (baseIndex-9), (baseIndex-1), baseIndex).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
            MinoType.Z -> {
                when (rotateType) {
                    RotateType.FIRST -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-9), baseIndex, (baseIndex+1), (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-1), baseIndex, (baseIndex+10), (baseIndex+11)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-10), (baseIndex-1), baseIndex, (baseIndex+9)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-11), (baseIndex-10), baseIndex, baseIndex+1).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
            MinoType.J ->  {
                when (rotateType) {
                    RotateType.FIRST -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), (baseIndex-9), baseIndex, (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-1), baseIndex, (baseIndex+1), (baseIndex+11)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-10), baseIndex, (baseIndex+9), (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-11), (baseIndex-1), baseIndex, (baseIndex+1)).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
            MinoType.L -> {
                when (rotateType) {
                    RotateType.FIRST -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), baseIndex, (baseIndex+10), (baseIndex+11)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-1), baseIndex, (baseIndex+1), (baseIndex+9)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-11), (baseIndex-10), baseIndex, (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-9), (baseIndex-1), baseIndex, (baseIndex+1)).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
            MinoType.T -> {
                when (rotateType) {
                    RotateType.FIRST -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), baseIndex, (baseIndex+1), (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.SECOND -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-1), baseIndex, (baseIndex+1), (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.THIRD -> {
                        val baseIndex = fallPositions[1]
                        listOf((baseIndex-10), (baseIndex-1), baseIndex, (baseIndex+10)).forEach {
                            positions.add(it)
                        }
                    }
                    RotateType.FOURTH -> {
                        val baseIndex = fallPositions[2]
                        listOf((baseIndex-10), (baseIndex-1), baseIndex, (baseIndex+1)).forEach {
                            positions.add(it)
                        }
                    }
                }
            }
        }
        return positions
    }

    // 消すラインを取得(0~19)
    fun getLine(): List<Int> {
        val list = mutableListOf<Int>()
        tetrisList.forEachIndexed { index, row ->
            if (row.size == COL) {
                list.add(index)
            }
        }
        return list
    }

    // 操作
    fun fall() {
        val newPositions = mutableListOf<Int>()
        getFallPositions().forEach {
            newPositions.add(it + 10)
            tetrisList[it/COL][it%COL] = BlockType.NONE.ordinal
        }
        newPositions.forEach {
            tetrisList[it/COL][it%COL] = BlockType.FALL.ordinal
        }
    }

    fun left() {
        val newPositions = mutableListOf<Int>()
        getFallPositions().forEach {
            newPositions.add(it - 1)
            tetrisList[it/COL][it%COL] = BlockType.NONE.ordinal
        }
        newPositions.forEach {
            tetrisList[it/COL][it%COL] = BlockType.FALL.ordinal
        }
    }

    fun right() {
        val newPositions = mutableListOf<Int>()
        getFallPositions().forEach {
            newPositions.add(it + 1)
            tetrisList[it/COL][it%COL] = BlockType.NONE.ordinal
        }
        newPositions.forEach {
            tetrisList[it/COL][it%COL] = BlockType.FALL.ordinal
        }
    }

    // 回転
    fun rotate() {
        if (currentType == MinoType.O) return
        val newPositions = getRotatePositions(currentType)
        getFallPositions().forEach {
            tetrisList[it/COL][it%COL] = BlockType.NONE.ordinal
        }
        newPositions.forEach {
            tetrisList[it/COL][it%COL] = BlockType.FALL.ordinal
        }
        rotateType = rotateType.nextType
    }

    // 操作できるミノをブロックに変換
    fun reach() {
        val fallPositions = getFallPositions()
        fallPositions.forEach {
            tetrisList[it/COL][it%COL] = currentType.block.ordinal
        }
    }
}