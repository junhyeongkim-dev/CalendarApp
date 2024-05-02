package com.android.calendarapp.util

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

object ResourceUtil {

    fun getString(context: Context, @StringRes id: Int): String {
        return context.getString(id)
    }

    fun getString(context: Context, @StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, *formatArgs)
    }

    fun getColor(context: Context, @ColorRes id: Int): Int {
        return context.getColor(id)
    }
}