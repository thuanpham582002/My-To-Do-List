package thuan.todolist.feature_todo.domain.use_case

import androidx.lifecycle.LiveData
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo

class GetGroupsToDo(private val repository: ToDoRepository) {
    operator fun invoke(): LiveData<List<GroupToDo>> = repository.getAllGroup()
}