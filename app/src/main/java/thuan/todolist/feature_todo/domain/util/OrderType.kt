package thuan.todolist.feature_todo.domain.util

sealed interface OrderType : ToDoOrder {
    object Ascending : OrderType
    object Descending : OrderType
}