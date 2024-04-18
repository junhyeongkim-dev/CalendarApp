package com.android.calendarapp.util

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceUtil {

    companion object {
        fun getString(context: Context, @StringRes id: Int): String {
            return context.getString(id)
        }

        fun getString(context: Context, @StringRes id: Int, vararg formatArgs: Any): String {
            return context.getString(id, *formatArgs)
        }
    }
}