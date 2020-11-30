package com.meme.chart.widget

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.meme.chart.Chart
import com.meme.chart.dp2pxf
import kotlin.math.ceil
import kotlin.math.max

/**
 * title: 柱状图
 * describe:
 *
 * @author memo
 * @date 2020-11-23 4:49 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class BarChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var data =
        if (isInEditMode) arrayListOf(
            Chart("第一个", 0.11f),
            Chart("第一个第", 0.425f),
            Chart("第一个第一", 0.525f),
            Chart("第一个第一个", 0.665f),
            Chart("第一个第一个第", 0.785f),
            Chart("第一个第一个第一", 0.905f))
        else arrayListOf()
        set(value) {
            field = value
            //重新计算和绘制
            requestLayout()
            anim.start()
        }

    /*** levels ***/
    var levels = if (isInEditMode) listOf("0", "2", "4", "6", "8", "10")
    else listOf("", "", "", "", "", "")
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /*** 名称一行最多显示几个字 ***/
    var maxLabelLineLength = 3
        set(value) {
            field = value
            requestLayout()
            invalidate()
        }

    /**
     * 设置柱状图颜色
     * @param startColor Int 底部起始颜色
     * @param endColor Int 顶部结束颜色
     */
    fun setBarColor(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        this.barStartColor = startColor
        this.barEndColor = endColor
        invalidate()
    }

    fun setBarColor(@ColorInt color: Int) {
        this.barStartColor = color
        this.barEndColor = color
        invalidate()
    }


    /*** 边框画笔 ***/
    private val mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 0.5f.dp2pxf
        this.strokeWidth = 0.5f.dp2pxf
    }

    /*** 文字画笔 ***/
    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 13.dp2pxf
    }

    /*** 虚线画笔 ***/
    private val mDashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 0.5f.dp2pxf
        // warning:这里必须要添加style，不然不显示
        style = Paint.Style.STROKE
        color = Color.parseColor("#999999")
        pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
    }

    /*** 柱状图画笔 ***/
    private val mBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    /*** 全局空白间隔 ***/
    private val blankHolder = 10.dp2pxf

    /*** 等级高度 ***/
    private val levelHeight = 30.dp2pxf

    /*** 用于计算文字的宽高 ***/
    private val textBounds = Rect()

    /*** 柱状图的宽度 ***/
    private val barWidth = 20.dp2pxf

    /*** 文字最多显示行数 ***/
    private var maxLabelLines = 1

    /*** 柱状图颜色 ***/
    private var barStartColor = Color.parseColor("#e41a2c")
    private var barEndColor = Color.parseColor("#faaf04")


    /*** 柱状图动画 ***/
    private var animPercent: Float = if (isInEditMode) 1f else 0f
        set(value) {
            field = value
            invalidate()
        }
    private val anim = ObjectAnimator.ofFloat(this, "animPercent", 0f, 1f)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 计算文字行数
        data.forEach {
            val lines = ceil(it.label.length / maxLabelLineLength.toFloat()).toInt()
            maxLabelLines = max(maxLabelLines, lines)
        }
        // 高度 = 空白 + Y轴 + 空白 + 文字 + 空白
        val curHeight = blankHolder * 3 + (levels.size - 1 + 0.5) * levelHeight + mTextPaint.fontSpacing * maxLabelLines
        setMeasuredDimension(measuredWidth, curHeight.toInt())
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制坐标轴
        var maxLengthText = ""
        levels.forEach { if (it.length > maxLengthText.length) maxLengthText = it }
        mTextPaint.getTextBounds(maxLengthText, 0, maxLengthText.length, textBounds)
        // 坐标轴Y 起点xy 终点xy
        val levelTextWidth = textBounds.width()
        val borderYStartX = blankHolder * 2 + levelTextWidth
        val borderYStartY = blankHolder
        val borderYEndX = borderYStartX
        val borderYEndY = borderYStartY + (levels.size - 1 + 0.5f) * levelHeight
        // 坐标轴X 起点xy 终点xy
        val borderXStartX = borderYEndX
        val borderXStartY = borderYEndY
        val borderXEndX = measuredWidth - blankHolder
        val borderXEndY = borderXStartY
        // 划线
        canvas.drawLine(borderYStartX, borderYStartY, borderYEndX, borderYEndY, mBorderPaint)
        canvas.drawLine(borderXStartX, borderXStartY, borderXEndX, borderXEndY, mBorderPaint)


        // 绘制百分比
        mTextPaint.textAlign = Paint.Align.RIGHT
        // 百分比文字起始右侧的X
        val textStartRightX = borderYStartX - blankHolder
        // 百分比文字起始右侧的Y
        var textStartRightY = borderYStartY + (0.5f * levelHeight) - (textBounds.top + textBounds.bottom) / 2f
        levels.reversed().forEach {
            canvas.drawText(it, textStartRightX, textStartRightY, mTextPaint)
            // 向下移动一格
            textStartRightY += levelHeight
        }


        // 绘制虚线
        // 虚线起始的X 向右移动边框画笔的宽度一半 防止重合
        val dashStartX = borderYStartX + mBorderPaint.strokeWidth / 2f
        // 虚线终点的X
        val dashEndX = borderXEndX
        // 虚线的Y
        var dashY = borderYStartY + 0.5f * levelHeight
        val path = Path()
        // warning:这里必须要使用path，使用drawLine不支持硬件加速
        levels.forEachIndexed { index, _ ->
            // 不用画最后一条
            if (index != levels.size - 1) {
                path.reset()
                path.moveTo(dashStartX, dashY)
                path.lineTo(dashEndX, dashY)
                canvas.drawPath(path, mDashPaint)
                // 向下移动一格
                dashY += levelHeight
            }
        }


        // 绘制柱状图 和 绘制底部文字
        if (data.isNullOrEmpty()) return
        mTextPaint.textAlign = Paint.Align.CENTER
        // 计算柱状图的空白分隔宽度
        val barBlankWidth = (borderXEndX - borderXStartX - data.size * barWidth) / (data.size * 2f)
        var left = borderXStartX
        var top: Float
        var right: Float
        val bottom = borderYEndY - mBorderPaint.strokeWidth / 2f
        // 柱状图的最大高度
        val maxBarHeight = levelHeight * (levels.size - 1)
        data.forEachIndexed { index, chart ->
            // 柱状图绘制
            left += barBlankWidth
            val percent = if (chart.percent > 1) 1f else chart.percent
            top = bottom - percent * maxBarHeight * animPercent
            right = left + barWidth
            val gradient = LinearGradient(
                left, bottom, left, top,
                barStartColor,
                barEndColor,
                Shader.TileMode.CLAMP
            )
            mBarPaint.shader = gradient
            canvas.drawRect(left, top, right, bottom, mBarPaint)
            left = right + barBlankWidth


            // 文字绘制
            val textX = right - barWidth / 2f
            var textY = borderXStartY + blankHolder - mTextPaint.fontMetrics.top
            var isTextDrawFinish = false
            for (i in 0 until maxLabelLines) {
                val start = i * maxLabelLineLength
                var end = (i + 1) * maxLabelLineLength
                if (isTextDrawFinish) {
                    return@forEachIndexed
                }
                if (end > chart.label.length) {
                    end = chart.label.length
                    isTextDrawFinish = true
                }
                val text = chart.label.substring(start, end)
                canvas.drawText(text, textX, textY, mTextPaint)
                textY += mTextPaint.fontSpacing
            }
        }

    }
}