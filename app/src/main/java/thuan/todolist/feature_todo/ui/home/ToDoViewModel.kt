package thuan.todolist.feature_todo.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos
import thuan.todolist.feature_todo.domain.model.ToDo


class ToDoViewModel(private val toDoRepository: ToDoRepository) : ViewModel() {
    private val savedStateHandle = SavedStateHandle()
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

    fun getAllToDo(): LiveData<List<ToDo>> =
        toDoRepository.getAllToDo()

    fun getAllToDoWithGroup(groupName: String): LiveData<List<GroupWithToDos>> =
        toDoRepository.getAllToDoWithGroup(groupName)

    fun getAllGroup(): LiveData<List<GroupToDo>> =
        toDoRepository.getAllGroup()


    fun insertToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepository.insertToDo(toDo)
    }

    fun insertGroup(group: GroupToDo) = viewModelScope.launch {
        toDoRepository.insertGroup(group)
    }

    fun deleteToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepository.deleteToDo(toDo)
    }

    fun updateToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepository.updateToDo(toDo)
    }

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