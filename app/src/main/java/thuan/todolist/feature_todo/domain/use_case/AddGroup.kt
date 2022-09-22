package thuan.todolist.feature_todo.domain.use_case

import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.InvalidGroupException

class AddGroup(private val toDoRepository: ToDoRepository) {

    @Throws(InvalidGroupException::class)  // annotation to indicate that this function can throw an exception
    suspend operator fun invoke(group: GroupToDo) {
        if (group.name.isEmpty())
            throw InvalidGroupException("The name of the group cannot be empty")
        toDoRepository.insertGroup(group)
    }
}
