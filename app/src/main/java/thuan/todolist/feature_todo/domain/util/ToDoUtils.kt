package thuan.todolist.feature_todo.domain.util

import thuan.todolist.MainActivity
import java.text.SimpleDateFormat
import java.util.*

object ToDoUtils {
    fun isExpired(dateAndTime: Date?): Boolean {
        if (dateAndTime == null) return false
        val dateAndTimeInMilliseconds = dateAndTime.time
        return dateAndTimeInMilliseconds < System.currentTimeMillis()
    }

    fun dateToString(date: Date?): String {
        MainActivity.SP_INSTANCE.let {
            val dateAndTimeFormat = it.getString("todo_date_and_time_format", "HH:mm dd/MM/yyyy")
            // get time not set string from resource
            return date?.let { date ->
                SimpleDateFormat(dateAndTimeFormat, Locale.getDefault()).format(date)
            } ?: MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.time_not_set)
        }
    }
}