package thuan.todolist.feature_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.data.repository.ToDoRepositoryImpl
import thuan.todolist.feature_todo.domain.model.ToDo


class ToDoViewModel(private val toDoRepositoryImpl: ToDoRepositoryImpl) : ViewModel() {

    fun getAllToDo(): LiveData<List<ToDo>> =
        toDoRepositoryImpl.getAllToDo();

    fun insertToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepositoryImpl.insertToDo(toDo)
    }

    fun deleteToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepositoryImpl.deleteToDo(toDo)
    }

    fun updateToDoItem(toDo: ToDo) = viewModelScope.launch {
        toDoRepositoryImpl.updateToDo(toDo)
    }
}

class ToDoViewModelFactory(private val toDoRepositoryImpl: ToDoRepositoryImpl) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ToDoViewModelFactory? = null

        fun getInstance(toDoRepositoryImpl: ToDoRepositoryImpl): ToDoViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ToDoViewModelFactory(toDoRepositoryImpl)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ToDoViewModel(toDoRepositoryImpl) as T
    }
}