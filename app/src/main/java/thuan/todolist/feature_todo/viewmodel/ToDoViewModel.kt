package thuan.todolist.feature_todo.viewmodel

import android.view.View
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionTracker
import androidx.room.Query
import kotlinx.coroutines.launch
import thuan.todolist.R
import thuan.todolist.feature_todo.data.repository.ToDoRepositoryImpl
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos
import thuan.todolist.feature_todo.domain.model.ToDo


class ToDoViewModel(private val toDoRepositoryImpl: ToDoRepositoryImpl) : ViewModel() {
    lateinit var trackerAdapterHomeFragment: SelectionTracker<Long>
    val savedStateHandle = SavedStateHandle()
    lateinit var searchQuery: String

    private fun removeSateTimeAndDate() {
        savedStateHandle.remove<String>("timeAndDate")
    }

    private fun saveStateToDo(toDo: ToDo) {
        savedStateHandle["toDo"] = toDo
    }

    private fun removeStateToDo() {
        savedStateHandle.remove<ToDo>("toDo")
    }

    fun goToEditToDoFragment(view: View, toDo: ToDo) {
        saveStateToDo(toDo)
    }

    fun goToAddToDoFragment(view: View) {
        removeStateToDo()
        removeSateTimeAndDate()
        view.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
    }

    fun getAllToDo(): LiveData<List<ToDo>> =
        toDoRepositoryImpl.getAllToDo();

    fun getAllToDoWithGroup(groupName: String): LiveData<List<GroupWithToDos>> =
        toDoRepositoryImpl.getAllToDoWithGroup(groupName)

    fun getAllGroup(): LiveData<List<GroupToDo>> =
        toDoRepositoryImpl.getAllGroup()


    fun insertToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepositoryImpl.insertToDo(toDo)
    }

    fun insertGroup(group: GroupToDo) = viewModelScope.launch {
        toDoRepositoryImpl.insertGroup(group)
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