package thuan.todolist.feature_todo.domain.use_case

import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo

class UpdateToDo(
    private val repository: ToDoRepository
) {
    suspend operator fun invoke(toDo: ToDo) {
        repository.updateToDo(toDo)
    }
}
