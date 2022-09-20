package thuan.todolist.feature_todo.domain.util

import androidx.lifecycle.LiveData
import thuan.todolist.feature_todo.domain.model.ToDo

sealed class ToDoTagType : ToDoOrder {
    object Title : ToDoTagType()
    object Date : ToDoTagType()
    object None : ToDoTagType()
}