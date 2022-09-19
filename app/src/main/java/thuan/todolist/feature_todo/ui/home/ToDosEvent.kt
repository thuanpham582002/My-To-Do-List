package thuan.todolist.feature_todo.ui.home

import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.util.ToDoOrder

sealed interface ToDosEvent {
    data class DeleteToDo(val toDo: ToDo) : ToDosEvent
    data class UpdateToDo(val toDo: ToDo) : ToDosEvent
    data class Order(val toDoOrder: ToDoOrder) : ToDosEvent
}