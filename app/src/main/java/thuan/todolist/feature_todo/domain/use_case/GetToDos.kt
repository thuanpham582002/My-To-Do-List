package thuan.todolist.feature_todo.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo

class GetToDos(private val repository: ToDoRepository) {
    operator fun invoke(): LiveData<List<ToDo>> {
        return Transformations.map(repository.getAllToDo()) {
            it.map { toDo -> toDo }
        }
    }
}