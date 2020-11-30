package com.meme.chart

import android.content.res.Resources
import android.util.Log

/**
 * title:
 * describe:
 *
 * @author memo
 * @date 2020-11-23 2:01 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
val Int.dp2px: Int get() = (Resources.getSystem().displayMetrics.density * this + 0.5f).toInt()
val Int.dp2pxf: Float get() = Resources.getSystem().displayMetrics.density * this + 0.5f
val Float.dp2px: Float get() = Resources.getSystem().displayMetrics.density * this + 0.5f
val Float.dp2pxf: Float get() = Resources.getSystem().displayMetrics.density * this + 0.5f

data class Chart(val label: String, val percent: Float)

fun log(tag: String, vararg contents: Any) {
    contents.forEach {
        Log.i(tag, it.toString())
    }
}