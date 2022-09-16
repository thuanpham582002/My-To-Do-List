package thuan.todolist.feature_todo.domain.use_case

import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo

class AddGroup(private val toDoRepository: ToDoRepository) {
    suspend operator fun invoke(group: GroupToDo) {
        toDoRepository.insertGroup(group)
    }
}
