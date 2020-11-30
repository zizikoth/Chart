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

        mBarChart.postDelayed({
            mBarChart.data = arrayListOf(
                Chart("周大侠", 0.8f),
                Chart("周二侠", 1f),
                Chart("周三侠", 0.3f),
                Chart("周四侠", 0.6f),
                Chart("周五侠周五侠周五侠周五侠", 0.4f)
            )
            mBarChart.levels = arrayListOf("10", "8", "6", "4", "2", "0")
        }, 2000)

        mLineChart.data = arrayListOf(
            Chart("周大侠", 1f),
            Chart("周二侠", 0.8f),
            Chart("周三侠", 0.2f),
            Chart("周四侠", 0.4f),
            Chart("周五侠周五侠周五侠周五侠", 0.6f)
        )

        mLineChart.postDelayed({
            mLineChart.data = arrayListOf(
                Chart("周大侠", 1f),
                Chart("周二侠", 0.8f),
                Chart("周三侠", 0.2f),
                Chart("周四侠", 0.5f),
                Chart("周五侠周五侠周五侠周五侠", 0.4f)
            )
            mLineChart.levels = arrayListOf("10", "8", "6", "4", "2", "0")
        }, 2000)

        mRingChart.data = arrayListOf(0.3f, 0.6f, 0.9f)
        mRingChart.postDelayed({
            mRingChart.data = arrayListOf(0.5f, 0.5f, 0.5f)
            mRingChart.ringBgColors = arrayListOf(Color.LTGRAY, Color.LTGRAY, Color.LTGRAY)
            mRingChart.ringColors = arrayListOf(Color.BLUE, Color.RED, Color.GREEN)
        }, 2000)

        mProgressChart.progress = 0.4f
        mProgressChart.progressText = "40%"
        mProgressChart.postDelayed({
            mProgressChart.progressText = "80%"
            mProgressChart.progress = 0.8f
        }, 2000)
    }
}