package com.meme.chart.progress

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.meme.chart.dp2pxf

/**
 * title: 进度条
 * describe:
 *
 * @author memo
 * @date 2020-12-04 10:20 AM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class OvalProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*** 进度条颜色 ***/
    @ColorInt
    var progressColor = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }

    /*** 进度条高度 ***/
    var progressHeight = 15.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 背景颜色 ***/
    @ColorInt
    var progressBgColor = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    /*** 背景高度 ***/
    var progressBgHeight = 25.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 进度文字颜色 ***/
    var progressTextColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    /*** 进度文字大小 ***/
    var progressTextSize = 7.dp2pxf
        set(value) {
            field = value
            invalidate()
        }

    /*** 进度 ***/
    @IntRange(from = 0, to = 100)
    var progress: Int = 0
        set(value) {
            val newValue = if (value < 0) 0 else if (value > 100) 100 else value
            if (enableAnim) {
                getProgressAnim(field, newValue).start()
            } else {
                progressPercent = newValue
            }
            field = newValue
        }

    /*** 是否允许进行动画 ***/
    var enableAnim = false

    /*** 动画时长 ***/
    var duration: Long = 500L

    private var progressPercent = progress
        set(value) {
            field = value
            invalidate()
        }

    private fun getProgressAnim(start: Int, end: Int): ObjectAnimator {
        return ObjectAnimator.ofInt(this, "progressPercent", start, end)
            .apply { duration = this@OvalProgress.duration }
    }


    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textAlign = Paint.Align.RIGHT }
    private val bgBound = RectF()
    private val progressBound = RectF()
    private val textBound = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, progressBgHeight.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制背景
        mPaint.color = progressBgColor
        bgBound.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
        canvas.drawRoundRect(bgBound, measuredHeight / 2f, measuredHeight / 2f, mPaint)

        // 绘制进度条
        mPaint.color = progressColor
        val padding = (progressBgHeight - progressHeight) / 2f
        val currentWidth = (measuredWidth - padding * 2 - progressHeight) * (progressPercent / 100f) + progressHeight + padding
        progressBound.set(padding, padding, currentWidth, progressHeight + padding)
        canvas.drawRoundRect(progressBound, progressHeight / 2f, progressHeight / 2f, mPaint)

        // 绘制文字
        mPaint.textSize = progressTextSize
        mPaint.color = progressTextColor
        mPaint.getTextBounds("0%", 0, 2, textBound)
        val textPadding = (progressHeight - textBound.width()) / 2f
        val text = "$progressPercent%"
        canvas.drawText(
            text,
            0,
            text.length,
            currentWidth - textPadding,
            measuredHeight / 2f - (textBound.top + textBound.bottom) / 2f,
            mPaint)
    }
}