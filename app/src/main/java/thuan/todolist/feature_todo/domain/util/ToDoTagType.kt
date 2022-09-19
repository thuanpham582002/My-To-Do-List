package thuan.todolist.feature_todo.domain.util

sealed interface ToDoTagType : ToDoOrder {
    object Title : ToDoTagType
    object Date : ToDoTagType
    object None : ToDoTagType
}