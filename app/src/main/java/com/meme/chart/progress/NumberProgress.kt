package com.meme.chart.progress

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import com.meme.chart.dp2pxf

/**
 * title:
 * describe:
 *
 * @author memo
 * @date 2020-12-04 11:33 AM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class NumberProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    /*** 进度条线颜色 ***/
    @ColorInt
    var lineColor = Color.BLUE
        set(value) {
            field = value
            invalidate()
        }

    /*** 进度条线高度 ***/
    var lineHeight = 2.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 进度文字颜色 ***/
    @ColorInt
    var textColor = Color.RED
        set(value) {
            field = value
            invalidate()
        }

    /*** 文字大小 ***/
    var textSize = 10.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 边距 ***/
    var padding = 5.dp2pxf
        set(value) {
            field = value
            invalidate()
        }

    /*** 是否允许动画 ***/
    var enableAnim = false

    /*** 进度 ***/
    @IntRange(from = 0, to = 100)
    var progress: Int = 100
        set(value) {
            val newValue = if (value < 0) 0 else if (value > 100) 100 else value
            if (enableAnim) {
                progressAnim.setIntValues(field,newValue)
                progressAnim.start()
            } else {
                progressPercent = newValue
            }
            field = newValue
        }

    /*** 显示的进度百分比 ***/
    private var progressPercent = progress
        set(value) {
            field = value
            invalidate()
        }

    /**
     * 获取进度动画
     */
    private val progressAnim =  ObjectAnimator.ofInt(this, "progressPercent",0)

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
        textAlign = Paint.Align.CENTER
    }
    private val textBound = Rect()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mPaint.textSize = textSize
        mPaint.getTextBounds("测试", 0, 2, textBound)
        setMeasuredDimension(measuredWidth, if (lineHeight > textBound.height()) lineHeight.toInt() else textBound.height())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val text = "$progressPercent%"
        mPaint.getTextBounds(text, 0, text.length, textBound)
        // 减去-文字宽度-文字边距*2-画笔宽度一半*2
        val lineWidth = measuredWidth - textBound.width() - padding * 2 - lineHeight
        val leftWidth = lineWidth * (progressPercent / 100f)
        val rightWidth = lineWidth - leftWidth
        val lineY = measuredHeight / 2f

        // 绘制线
        mPaint.strokeWidth = lineHeight
        mPaint.color = lineColor
        if (progressPercent != 0) {
            canvas.drawLine(lineHeight / 2f, lineY, leftWidth, lineY, mPaint)
        }
        if (progressPercent != 100) {
            canvas.drawLine(measuredWidth - rightWidth, lineY, measuredWidth - lineHeight / 2f, lineY, mPaint)
        }

        // 绘制进度
        mPaint.color = textColor
        mPaint.textSize = textSize
        val textX = (leftWidth + (measuredWidth - rightWidth)) / 2f
        val textY = measuredHeight / 2f - (textBound.top + textBound.bottom) / 2f
        canvas.drawText(text, 0, text.length, textX, textY, mPaint)
    }

}