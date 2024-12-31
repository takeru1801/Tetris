package com.flow.tetris.presentation.play

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.flow.tetris.core.BlockType
import com.flow.tetris.core.Drawables
import com.flow.tetris.core.MinoType
import com.flow.tetris.core.RotateType
import com.flow.tetris.core.Tetris
import com.flow.tetris.core.currentType
import com.flow.tetris.core.fallTime
import com.flow.tetris.core.rotateType
import com.flow.tetris.core.tetrisList
import com.flow.tetris.databinding.ActivityPlayBinding
import java.util.Timer
import java.util.TimerTask

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding

    private lateinit var playAdapter: PlayAdapter

    private val timer = Timer()
    private lateinit var task: TimerTask
    private val handler = Handler(Looper.getMainLooper())

    private var nextMinos = mutableListOf<MinoType>()
    private var updatePositionList = mutableListOf<Int>()

    private var isGameOver: Boolean = false
        set(value) {
            field = value
            if (value) {
                gameOver()
            }
        }
    private var isFall: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        setAdapter()
        setTimer()
        setListener()

        play()
    }

    override fun onDestroy() {
        super.onDestroy()
        Tetris.resetTetrisList()
        timer.cancel()
    }

    private fun setup() {
        nextMinos = Tetris.getNextMinos().toMutableList()
    }

    private fun play() {
        timer.schedule(task, 1000, (fallTime*1000).toLong())
    }

    // テトリス
    private fun setAdapter() {
        playAdapter = PlayAdapter(tetrisList)
        Drawables.setBackground(binding.recyclerView)
        binding.recyclerView.apply {
            layoutManager = object : GridLayoutManager(this@PlayActivity, Tetris.COL) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            addItemDecoration(DividerItemDecoration(this@PlayActivity, DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(this@PlayActivity, DividerItemDecoration.HORIZONTAL))
            adapter = playAdapter
        }
    }

    private fun setTimer() {
        task = object : TimerTask() {
            override fun run() {
                updatePositionList.clear()
                val tempTetrisList = Tetris.getCloneTetrisList()

                if (isFall) {
                    if (nextMinos.isEmpty()) {
                        nextMinos = Tetris.getNextMinos().toMutableList()
                    }
                    currentType = nextMinos.first()
                    rotateType = RotateType.FIRST
                    nextMinos.removeAt(0)
                    currentType.firstPosition.forEach {
                        // ミノが配置できない -> ゲームオーバー
                        if (tetrisList[it/Tetris.COL][it%Tetris.COL] != BlockType.NONE.ordinal) {
                            isGameOver = true
                            timer.cancel()
                        } else {
                            tetrisList[it/Tetris.COL][it%Tetris.COL] = BlockType.FALL.ordinal
                        }
                    }
                    isFall = false
                }
                // 落下中
                else {
                    // 落下した
                    if (Tetris.isReachBottom()) {
                        isFall = true
                        Tetris.reach()
                    } else {
                        Tetris.fall()
                    }
                }
                updatePositionList = Tetris.getUpdatePositions(tempTetrisList).toMutableList()

                handler.post {
                    if (isGameOver) return@post
                    // UI更新処理
                    Log.d("Log: ", "update:${updatePositionList}")
//                    updatePositionList.forEach {
//                        playAdapter.notifyItemChanged(it)
//                    }
                    playAdapter.update()
                }
            }
        }
    }

    private fun setListener() {
        binding.leftButton.setOnClickListener {
            if (!Tetris.isReachLeft()) {
                Tetris.left()
                playAdapter.update()
            }
        }

        binding.rightButton.setOnClickListener {
            if (!Tetris.isReachRight()) {
                Tetris.right()
                playAdapter.update()
            }
        }

        binding.rotateButton.setOnClickListener {
            if (currentType == MinoType.O) return@setOnClickListener
            val nextRotatePositions = Tetris.getRotatePositions(currentType)
            if (Tetris.isRotate(nextRotatePositions, currentType, rotateType)) {
                Tetris.rotate()
                playAdapter.update()
            }
        }
    }

    private fun gameOver() {
        Log.d("Log: ", "GameOver")
    }
}