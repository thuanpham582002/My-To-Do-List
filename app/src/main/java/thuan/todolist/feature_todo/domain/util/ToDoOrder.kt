package thuan.todolist.feature_todo.domain.util

sealed interface ToDoOrder {
    data class Order(
        val todoType: ToDoType = ToDoType.All,
        val groupType: GroupType = GroupType.All,
        val todoTagType: ToDoTagType = ToDoTagType.None,
        val orderType: OrderType = OrderType.Ascending,
        val searchOrder: SearchOrder = SearchOrder("")
    ) : ToDoOrder
}
