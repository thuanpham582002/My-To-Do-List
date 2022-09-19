package thuan.todolist.feature_todo.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.util.*

const val TAG = "ToDoUseCase"

class GetToDos(private val repository: ToDoRepository) {
    operator fun invoke(toDoOrder: ToDoOrder.Order): LiveData<List<ToDo>> {
        when (toDoOrder.groupType) {
            is GroupType.All -> {
                when (toDoOrder.todoType) {
                    is ToDoType.All -> {
                        return Transformations.map(repository.getAllToDo()) { list ->
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            list.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            list.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            list
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            list.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            list.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            list
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is ToDoType.Completed -> {
                        return Transformations.map(repository.getAllToDo()) { listToDo ->
                            val listToDoCompleted = listToDo.filter { toDo -> toDo.isCompleted }
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoCompleted.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoCompleted.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoCompleted
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoCompleted.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoCompleted.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoCompleted
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is ToDoType.Uncompleted -> {
                        return Transformations.map(repository.getAllToDo()) { listToDo ->
                            val listToDoUncompleted = listToDo.filter { toDo -> !toDo.isCompleted }
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoUncompleted.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoUncompleted.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoUncompleted
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoUncompleted.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoUncompleted.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoUncompleted
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is GroupType.Custom -> {
                return Transformations.map(repository.getAllToDoWithGroup(toDoOrder.groupType.groupName)) { groupWithToDos ->
                    val listToDo = groupWithToDos.todos
                    when (toDoOrder.todoType) {
                        is ToDoType.All -> {
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDo.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDo.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDo
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDo.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDo.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDo
                                        }
                                    }
                                }
                            }
                        }
                        is ToDoType.Completed -> {
                            val listToDoCompleted = listToDo.filter { toDo -> toDo.isCompleted }
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoCompleted.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoCompleted.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoCompleted
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoCompleted.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoCompleted.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoCompleted
                                        }
                                    }
                                }
                            }
                        }
                        is ToDoType.Uncompleted -> {
                            val listToDoUncompleted = listToDo.filter { toDo -> !toDo.isCompleted }
                            when (toDoOrder.orderType) {
                                is OrderType.Ascending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoUncompleted.sortedBy { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoUncompleted.sortedBy { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoUncompleted
                                        }
                                    }
                                }
                                is OrderType.Descending -> {
                                    when (toDoOrder.todoTagType) {
                                        is ToDoTagType.Title -> {
                                            listToDoUncompleted.sortedByDescending { it.title }
                                        }
                                        is ToDoTagType.Date -> {
                                            listToDoUncompleted.sortedByDescending { it.dateAndTime }
                                        }

                                        is ToDoTagType.None -> {
                                            listToDoUncompleted
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}