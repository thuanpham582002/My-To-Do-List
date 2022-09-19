package thuan.todolist.feature_todo.domain.util

sealed interface ToDoType : ToDoOrder {
    object All : ToDoType
    object Completed : ToDoType
    object Uncompleted : ToDoType
}