package thuan.todolist.feature_todo.domain.util

import android.content.Context
import androidx.preference.PreferenceManager
import java.text.SimpleDateFormat
import java.util.*

object ToDoUtils {
    fun isExpired(dateAndTime: Date?): Boolean {
        if (dateAndTime == null) return false
        val dateAndTimeInMilliseconds = dateAndTime.time
        return dateAndTimeInMilliseconds < System.currentTimeMillis()
    }

    fun dateToString(context: Context, date: Date?): String {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        val dateAndTimeFormat = sp.getString("todo_date_and_time_format", "HH:mm dd/MM/yyyy")
        // get time not set string from resource
        return date?.let {
            SimpleDateFormat(dateAndTimeFormat, Locale.getDefault()).format(it)
        } ?: context.resources.getString(thuan.todolist.R.string.time_not_set)
    }
}
