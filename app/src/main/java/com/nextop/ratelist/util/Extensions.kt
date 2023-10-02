package com.nextop.ratelist.util

import android.content.res.Resources
import java.text.DecimalFormat
import kotlin.math.roundToInt

fun Float.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Double.fractionFormat(fractionDigits: Short, groupingSize: Int = 0): String {
    val df = DecimalFormat()
    df.groupingSize = groupingSize
    df.maximumFractionDigits = fractionDigits.toInt()
    df.minimumFractionDigits = fractionDigits.toInt()
    return df.format(this)
}