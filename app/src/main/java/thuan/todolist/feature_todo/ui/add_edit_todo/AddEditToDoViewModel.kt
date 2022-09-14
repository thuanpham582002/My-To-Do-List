package thuan.todolist.feature_todo.ui.add_edit_todo

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.ToDo

class AddEditToDoViewModel(private val toDoRepository: ToDoRepository, private val toDo: ToDo?) :
    ViewModel() {
    val todoTitle = MutableLiveData("")

    val todoDescription = MutableLiveData("")

    val todoDateAndTime = MutableLiveData("Date not set")

    val groupName = MutableLiveData("Default")

    init {
        todoTitle.value = toDo?.title
        todoDescription.value = toDo?.description
        todoDateAndTime.value = toDo?.dateAndTime
        groupName.value = toDo?.groupName
    }

    fun getCurrentToDo(): ToDo {
        return ToDo(
            id = toDo?.id ?: 0,
            title = todoTitle.value ?: "",
            description = todoDescription.value ?: "",
            dateAndTime = todoDateAndTime.value ?: "",
            isCompleted = toDo?.isCompleted ?: false,
            groupName = groupName.value ?: ""
        )
    }

    fun onEvent(event: AddEditToDoEvent) {
        when (event) {
            is AddEditToDoEvent.EnteredTitle -> {
                todoTitle.value = event.title
            }

            is AddEditToDoEvent.EnteredDescription -> {
                todoDescription.value = event.description
            }

            is AddEditToDoEvent.EnteredDateAndTime -> {
                todoDateAndTime.value = event.dateAndTime
            }

            is AddEditToDoEvent.EnteredGroupName -> {
                groupName.value = event.groupName
            }

            is AddEditToDoEvent.SaveToDo -> {
                saveToDo(event.toastCallBack, event.pressBackCallBack)
            }
        }
    }

    private fun saveToDo(toastCallBack: (String) -> Unit, pressBackCallBack: () -> Unit) {
        val title = todoTitle.value
        val description = todoDescription.value
        val dateAndTime = todoDateAndTime.value
        val groupName = groupName.value
        if (title.toString().isEmpty()) {
            toastCallBack("Title cannot be empty")
            return
        }
        if (toDo!!.id == -1) {
            insertToDo(
                ToDo(
                    id = 0,
                    title = title.toString(),
                    description = description.toString(),
                    dateAndTime = dateAndTime.toString(),
                    isCompleted = false,
                    groupName = groupName.toString()
                )
            )
            pressBackCallBack()
            toastCallBack("ToDo added")
        } else {
            updateToDo(
                ToDo(
                    id = toDo.id,
                    title = title.toString(),
                    description = description.toString(),
                    dateAndTime = dateAndTime.toString(),
                    isCompleted = toDo.isCompleted,
                    groupName = groupName.toString()
                )
            )
            pressBackCallBack()
            toastCallBack("ToDo updated")
        }
    }

    fun getGroupToDo(): LiveData<List<GroupToDo>> {
        return toDoRepository.getAllGroup()
    }

    fun insertGroup(groupToDo: GroupToDo) {
        viewModelScope.launch {
            toDoRepository.insertGroup(groupToDo)
        }
    }

    private fun insertToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepository.insertToDo(toDo)
    }


    private fun updateToDo(toDo: ToDo) = viewModelScope.launch {
        toDoRepository.updateToDo(toDo)
    }

}

@Suppress("UNCHECKED_CAST")
class AddEditToDoViewModelFactory(
    private val toDoRepository: ToDoRepository,
    private val toDo: ToDo?
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        fun getInstance(
            toDoRepository: ToDoRepository,
            toDo: ToDo?
        ): AddEditToDoViewModelFactory =
            synchronized(this) {
                AddEditToDoViewModelFactory(toDoRepository, toDo)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditToDoViewModel(toDoRepository, toDo) as T
    }
}
