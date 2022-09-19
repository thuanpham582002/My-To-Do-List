package thuan.todolist.feature_todo.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.util.*


class ToDoViewModel(private val toDoRepository: ToDoRepository) : ViewModel() {
    private val savedStateHandle = SavedStateHandle()
    private val toDoUseCases = Injection.provideToDoUseCases(toDoRepository)
    var listTodo = MutableLiveData<List<ToDo>>()
    var toDoOrder: MutableLiveData<ToDoOrder.Order> = MutableLiveData(ToDoOrder.Order())
    val defaultTodo = ToDo(
        id = -1,
        title = "",
        description = "",
        dateAndTime = "Time not set",
        isCompleted = false,
        groupName = "Default"
    )

    fun goToAddAndEditToDoFragment(view: View, toDo: ToDo) {
        Log.i("Viewmodel", "goToAddAndEditToDoFragment: $toDo")
        view.findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment(toDo))
    }

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

                    else -> {}
                }
            }
        }
    }

//    fun updateToDo(toDo: ToDo) = viewModelScope.launch {
//        toDoRepository.updateToDo(toDo)
//    }

}

class ToDoViewModelFactory(private val toDoRepository: ToDoRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ToDoViewModelFactory? = null

        fun getInstance(toDoRepository: ToDoRepository): ToDoViewModelFactory =
            instance ?: synchronized(this) {
                ToDoViewModelFactory(toDoRepository).apply { instance = this }
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToDoViewModel(toDoRepository) as T
    }
}