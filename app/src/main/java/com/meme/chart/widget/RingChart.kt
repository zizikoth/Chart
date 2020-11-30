package com.meme.chart.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.meme.chart.dp2pxf

/**
 * title:环形图
 * describe:
 *
 * @author memo
 * @date 2020-11-27 5:28 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class RingChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    /*** 圆环数据 ***/
    var data = arrayListOf<Float>(0.23f, 0.45f, 0.56f, 0.67f)
        set(value) {
            field = value
            // 设置初始化颜色
            ringBgColors = ArrayList(data.map { Color.LTGRAY })
            ringColors = ArrayList(data.map { Color.BLACK })
            requestLayout()
            anim.start()
        }

    /*** 每一条圆环的宽度 ***/
    var ringWidth = 20.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 圆环的背景颜色 ***/
    var ringBgColors = arrayListOf(
        Color.LTGRAY,
        Color.LTGRAY,
        Color.LTGRAY,
        Color.LTGRAY)
        set(value) {
            field = value
            invalidate()
        }

    /*** 圆环的颜色 ***/
    var ringColors = arrayListOf(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK)
        set(value) {
            field = value
            invalidate()
        }

    @ColorInt
    var innerRingColor = Color.WHITE
        set(value) {
            field = value
            invalidate()
        }

    /*** 内部空心园的半径 ***/
    var innerRingRadius = 10.dp2pxf
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }

    private val rectF = RectF()

    private var animPercent = if (isInEditMode) 1f else 0f
        set(value) {
            field = value
            invalidate()
        }
    private val anim = ObjectAnimator.ofFloat(this, "animPercent", 0f, 1f)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 计算宽高 (内部园半径+所有圆环的宽度)*2
        val size = (innerRingRadius + data.size * ringWidth).toInt() * 2
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isNullOrEmpty()) return
        if (data.size > ringColors.size || data.size > ringBgColors.size) return

        // 圆心
        val circleX = measuredWidth / 2f
        val circleY = measuredHeight / 2f

        // 绘制内部园
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 1f
        mPaint.color = innerRingColor
        canvas.drawCircle(circleX, circleY, innerRingRadius, mPaint)


        //绘制圆环
        mPaint.strokeWidth = ringWidth
        mPaint.style = Paint.Style.STROKE
        data.forEachIndexed { index, percent ->
            val radius = innerRingRadius + (index + 0.5f) * ringWidth
            // 绘制圆环背景
            mPaint.color = ringBgColors[index]
            canvas.drawCircle(circleX, circleY, radius, mPaint)

            // 绘制圆环前景
            mPaint.color = ringColors[index]
            rectF.set(circleX - radius, circleY - radius, circleX + radius, circleY + radius)
            val sweepAngle = 360f * if (percent > 1) 1f else percent
            canvas.drawArc(rectF, -90f, (sweepAngle * animPercent), false, mPaint)
        }

    }


}