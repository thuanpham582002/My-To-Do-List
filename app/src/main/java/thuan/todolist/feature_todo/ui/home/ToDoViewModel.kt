package thuan.todolist.feature_todo.ui.home

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.use_case.ToDoUseCases
import thuan.todolist.feature_todo.domain.util.*


class ToDoViewModel(private val toDoUseCases: ToDoUseCases) : ViewModel() {
    private val savedStateHandle = SavedStateHandle()
    val toDoOrder: MutableLiveData<ToDoOrder.Order> = MutableLiveData(ToDoOrder.Order())
    var listTodo: LiveData<List<ToDo>> = Transformations.switchMap(toDoOrder) { order ->
        Log.i(TAG, ": track to do")
        toDoUseCases.getToDos(order)
    }

    val defaultTodo = ToDo(
        id = -1,
        title = "",
        description = "",
        dateAndTime = "Time not set",
        isCompleted = false,
        groupName = "Default"
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