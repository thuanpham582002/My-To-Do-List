package thuan.todolist.feature_todo.domain.util

sealed interface GroupType : ToDoOrder {
    object All : GroupType
    class Custom(val groupName: String) : GroupType
}
