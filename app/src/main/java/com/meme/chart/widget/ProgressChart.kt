package com.meme.chart.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.meme.chart.dp2pxf

/**
 * title: 百分比圆环
 * describe:
 *
 * @author memo
 * @date 2020-11-30 5:13 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class ProgressChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*** 百分比进度 ***/
    var progress: Float = if (isInEditMode) 0.5f else 0f
        set(value) {
            val newValue = if (value < 0) 0f else if (value > 1) 1f else value
            progressAnim.setFloatValues(field,newValue)
            progressAnim.start()
            field = newValue
        }

    /*** 圆环的背景颜色 ***/
    var progressBgColor = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    /*** 百分比圆环的颜色 ***/
    var progressColor = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }

    /*** 内部园的半径 ***/
    var progressInnerRadius = 50.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 圆环的宽度 ***/
    var progressWidth = 20.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 内部显示的文字 ***/
    var progressText = if (isInEditMode) "50%" else ""
        set(value) {
            field = value
            invalidate()
        }

    /*** 内部显示文字的颜色 ***/
    var progressTextColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    /*** 内部显示文字的大小 ***/
    var progressTextSize = 14.dp2pxf
        set(value) {
            field = value
            invalidate()
        }

    /*** 百分比的临时变量 用于动画展示 ***/
    private var currentProgress = progress
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 创建一个动画
     */
    private val progressAnim =  ObjectAnimator.ofFloat(this, "currentProgress",0f)

    /*** 圆环的方形 ***/
    private val rect = RectF()

    /*** 计算文字的宽高 ***/
    private val textBound = Rect()

    /*** 圆环的画笔 ***/
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }

    /*** 文字的画笔 ***/
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = ((progressWidth + progressInnerRadius) * 2).toInt()
        setMeasuredDimension(size, size)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = measuredWidth / 2f
        val centerY = measuredHeight / 2f

        // 绘制背景
        mPaint.color = progressBgColor
        mPaint.strokeWidth = progressWidth
        val radius = progressInnerRadius + progressWidth / 2f
        canvas.drawCircle(centerX, centerY, radius, mPaint)

        // 绘制分数
        mPaint.color = progressColor
        rect.set(progressWidth / 2f, progressWidth / 2f, measuredHeight - progressWidth / 2f, measuredWidth - progressWidth / 2f)
        canvas.drawArc(
            rect,
            -90f,
            currentProgress * 360f,
            false,
            mPaint
        )

        // 绘制文字
        mTextPaint.textSize = progressTextSize
        mTextPaint.color = progressTextColor
        mTextPaint.getTextBounds(progressText, 0, progressText.length, textBound)
        canvas.drawText(progressText, centerX, centerY - (textBound.top + textBound.bottom) / 2f, mTextPaint)

    }


}