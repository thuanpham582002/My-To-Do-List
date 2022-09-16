package thuan.todolist.feature_todo.domain.use_case

import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo

class DeleteToDo(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(todo: ToDo) {
        repository.deleteToDo(todo)
    }
}
