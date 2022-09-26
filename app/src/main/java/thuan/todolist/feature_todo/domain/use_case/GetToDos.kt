package thuan.todolist.feature_todo.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.util.*

class GetToDos(private val repository: ToDoRepository) {
    operator fun invoke(toDoOrder: ToDoOrder.Order): LiveData<List<ToDo>> {
        var listLiveDataToDo: LiveData<List<ToDo>>

        listLiveDataToDo = when (toDoOrder.groupType) {
            is GroupType.All -> repository.getAllToDo()
            is GroupType.Custom -> Transformations.map(repository.getAllToDoWithGroup(toDoOrder.groupType.groupName)) { it.todos }
        }

        when (toDoOrder.todoType) {
            is ToDoType.All -> {}
            is ToDoType.Completed -> listLiveDataToDo =
                Transformations.map(listLiveDataToDo) { listToDo -> listToDo.filter { it.isCompleted } }
            is ToDoType.Uncompleted -> listLiveDataToDo =
                Transformations.map(listLiveDataToDo) { listToDo -> listToDo.filter { !it.isCompleted } }
        }

        listLiveDataToDo = Transformations.map(listLiveDataToDo) { listToDo ->
            listToDo.filter {
                it.title.lowercase().contains(toDoOrder.searchOrder.searchQuery.lowercase())
            }
        }

        when (toDoOrder.orderType) {
            is OrderType.Ascending -> {
                when (toDoOrder.todoTagType) {
                    is ToDoTagType.Title -> listLiveDataToDo =
                        Transformations.map(listLiveDataToDo) { listToDo ->
                            listToDo.sortedBy { it.title }
                        }
                    is ToDoTagType.Date -> listLiveDataToDo =
                        Transformations.map(listLiveDataToDo) { listToDo ->
                            listToDo.sortedBy { it.dateAndTime }
                        }

                    is ToDoTagType.None -> {
                        listLiveDataToDo =
                            Transformations.map(listLiveDataToDo) { listToDo ->
                                listToDo.sortedBy { it.id }
                            }
                    }
                }
            }
            is OrderType.Descending -> {
                when (toDoOrder.todoTagType) {
                    is ToDoTagType.Title -> listLiveDataToDo =
                        Transformations.map(listLiveDataToDo) { listToDo ->
                            listToDo.sortedByDescending { it.title }
                        }
                    is ToDoTagType.Date -> listLiveDataToDo =
                        Transformations.map(listLiveDataToDo) { listToDo ->
                            listToDo.sortedByDescending { it.dateAndTime }
                        }

                    is ToDoTagType.None -> {
                        listLiveDataToDo =
                            Transformations.map(listLiveDataToDo) { listToDo ->
                                listToDo.sortedByDescending { it.id }
                            }
                    }
                }
            }
        }

        return listLiveDataToDo
    }
}