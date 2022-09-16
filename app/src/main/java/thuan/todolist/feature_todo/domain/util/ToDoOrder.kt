package thuan.todolist.feature_todo.domain.util

sealed class ToDoOrder(orderType: OrderType) {
    class Title(orderType: OrderType) : ToDoOrder(orderType)
    class Date(orderType: OrderType) : ToDoOrder(orderType)
}
