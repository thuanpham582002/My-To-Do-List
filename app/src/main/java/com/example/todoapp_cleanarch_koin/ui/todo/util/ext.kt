package com.example.todoapp_cleanarch_koin.ui.todo.util

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.*

fun Date?.toFormattedString(context: Context): String {
    val sp = PreferenceManager.getDefaultSharedPreferences(context)
//    val dateFormat = sp.getString(APP_DATE_FORMAT, "HH:mm dd/MM/yyyy")
    val dateFormat = "dd/MM/yyyy"
//    if (this == null) return context.resources.getString(R.string.time_not_set)
//    return java.text.SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)
    if (this == null) return ""
    return java.text.SimpleDateFormat(dateFormat, Locale.getDefault()).format(this)

}