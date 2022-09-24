package thuan.todolist.feature_todo.domain.util

import java.text.SimpleDateFormat
import java.util.*

object ToDoUtils {
    fun isExpired(dateAndTime: String): Boolean {
        if (dateAndTime == "Time not set") return false
        val dateAndTimeInMiliseconds =
            SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault()).parse(dateAndTime)!!.time
        return dateAndTimeInMiliseconds < System.currentTimeMillis()
    }
}