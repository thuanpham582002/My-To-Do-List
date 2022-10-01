package thuan.todolist.feature_todo.ui.add_edit_todo.utils

import java.io.Serializable
import java.util.*

interface ActionSetTime : Serializable {
    fun setTime(dateAndTime: Date?)
}