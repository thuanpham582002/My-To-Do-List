package thuan.todolist.feature_todo.domain.use_case

import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.InvalidToDoException
import thuan.todolist.feature_todo.domain.model.ToDo
import kotlin.jvm.Throws

class AddToDo(
    private val repository: ToDoRepository
) {

    @Throws(InvalidToDoException::class)
    suspend operator fun invoke(toDo: ToDo): Long {
        if (toDo.title.isBlank()) {
            throw InvalidToDoException("The title of the todo can't be empty")
        }
        return repository.insertToDo(toDo)
    }
}
