package thuan.todolist.feature_todo.ui.home

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.use_case.ToDoUseCases
import thuan.todolist.feature_todo.domain.util.*


class ToDoViewModel(private val toDoUseCases: ToDoUseCases) : ViewModel() {
    companion object {
        var instance: ToDoViewModel? = null
    }
    init {
        instance = this
    }

    private val toDoOrder: MutableLiveData<ToDoOrder.Order> = MutableLiveData(ToDoOrder.Order())
    var listTodo: LiveData<List<ToDo>> = Transformations.switchMap(toDoOrder) { order ->
            toDoUseCases.getToDos(order)
    }

    val notifiItemPos = MutableLiveData<Int>()


    val isFilterVisible: MutableLiveData<Boolean> = MutableLiveData(false)

    val defaultTodo = ToDo(
        id = -1,
        title = "",
        description = "",
        dateAndTime = null,
        isCompleted = false,
        groupName = "Default",
        isExpired = false
    )

    fun onEvent(event: ToDosEvent) {
        when (event) {
            is ToDosEvent.DeleteToDo -> {
                viewModelScope.launch {
                    toDoUseCases.deleteToDo(event.toDo)
                }
            }

            is ToDosEvent.UpdateToDo -> {
                viewModelScope.launch {
                    toDoUseCases.updateToDo(event.toDo)
                }
            }

            is ToDosEvent.Order -> {
                when (event.toDoOrder) {
                    is GroupType -> {
                        toDoOrder.value = toDoOrder.value?.copy(
                            groupType = event.toDoOrder
                        )
                    }
                    is OrderType -> {
                        toDoOrder.value = toDoOrder.value?.copy(
                            orderType = event.toDoOrder
                        )
                    }

                    is ToDoType -> {
                        toDoOrder.value = toDoOrder.value?.copy(
                            todoType = event.toDoOrder
                        )
                    }

                    is ToDoTagType -> {
                        toDoOrder.value = toDoOrder.value?.copy(
                            todoTagType = event.toDoOrder
                        )
                    }

                    is SearchOrder -> {
                        toDoOrder.value = toDoOrder.value?.copy(
                            searchOrder = event.toDoOrder
                        )
                    }

                    is ToDoOrder.Order -> {
                    }
                }
            }
        }
    }

    fun getGroupToDo(): LiveData<List<GroupToDo>> {
        return toDoUseCases.getGroupsToDo()
    }

    override fun onCleared() {
        super.onCleared()
        instance = null
    }
}

@Suppress("UNCHECKED_CAST")
class ToDoViewModelFactory(private val toDoUseCases: ToDoUseCases) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ToDoViewModelFactory? = null

        fun getInstance(toDoUseCases: ToDoUseCases): ToDoViewModelFactory =
            instance ?: synchronized(this) {
                ToDoViewModelFactory(toDoUseCases).apply { instance = this }
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToDoViewModel(toDoUseCases) as T
    }
}
