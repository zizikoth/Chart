package com.meme.chart

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBarChart.data = arrayListOf(
            Chart("周大侠", 1f),
            Chart("周二侠", 0.8f),
            Chart("周三侠", 0.2f),
            Chart("周四侠", 0.6f),
            Chart("周五侠周五侠周五侠周五侠", 0.3f)
        )

        mLineChart.data = arrayListOf(
            Chart("周大侠", 1f),
            Chart("周二侠", 0.8f),
            Chart("周三侠", 0.2f),
            Chart("周四侠", 0.4f),
            Chart("周五侠周五侠周五侠周五侠", 0.6f)
        )

        mRingChart.data = arrayListOf(0.3f, 0.6f, 0.9f)

        mProgressChart.progress = 0.4f
        mProgressChart.progressText = "40%"

        mOvalProgress.progress = 90

        mNumberProgress.progress = 0

        mBarChart.postDelayed({
            delay()
        }, 2000)
    }

    private fun delay() {
        mBarChart.data = arrayListOf(
            Chart("周大侠", 0.8f),
            Chart("周二侠", 1f),
            Chart("周三侠", 0.3f),
            Chart("周四侠", 0.6f),
            Chart("周五侠周五侠周五侠周五侠", 0.4f)
        )
        mBarChart.levels = arrayListOf("10", "8", "6", "4", "2", "0")


        mLineChart.data = arrayListOf(
            Chart("周大侠", 1f),
            Chart("周二侠", 0.8f),
            Chart("周三侠", 0.2f),
            Chart("周四侠", 0.5f),
            Chart("周五侠周五侠周五侠周五侠", 0.4f)
        )
        mLineChart.levels = arrayListOf("10", "8", "6", "4", "2", "0")


        mRingChart.data = arrayListOf(0.5f, 0.5f, 0.5f)
        mRingChart.ringBgColors = arrayListOf(Color.LTGRAY, Color.LTGRAY, Color.LTGRAY)
        mRingChart.ringColors = arrayListOf(Color.BLUE, Color.RED, Color.GREEN)


        mProgressChart.progressText = "80%"
        mProgressChart.progress = 0.8f


        mOvalProgress.enableAnim = true
        mOvalProgress.progress = 10

        mNumberProgress.enableAnim = true
        mNumberProgress.progress = 100
    }
}