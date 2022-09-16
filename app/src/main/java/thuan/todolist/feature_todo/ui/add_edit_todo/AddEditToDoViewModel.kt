package thuan.todolist.feature_todo.ui.add_edit_todo

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.ToDo

class AddEditToDoViewModel(private val toDoRepository: ToDoRepository, private val toDo: ToDo?) :
    ViewModel() {
    private val toDoUseCases = Injection.provideToDoUseCases(toDoRepository)

    val latestState = MutableLiveData<UIEvent>()

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

            is AddEditToDoEvent.SaveGroup -> {
                viewModelScope.launch {
                    toDoUseCases.addGroup(GroupToDo(name = event.groupName))
                    latestState.value = UIEvent.ShowSnackBar("Group added")
                }
            }

            is AddEditToDoEvent.SaveToDo -> {
                saveToDo()
            }
        }
    }

    private fun saveToDo() {
        val title = todoTitle.value
        val description = todoDescription.value
        val dateAndTime = todoDateAndTime.value
        val groupName = groupName.value
        if (title.toString().isEmpty()) {
            latestState.value = UIEvent.ShowSnackBar("Title cannot be empty")
            return
        }

        if (toDo!!.id == -1) {
            viewModelScope.launch {
                toDoUseCases.addToDo(
                    ToDo(
                        id = 0,
                        title = title.toString(),
                        description = description.toString(),
                        dateAndTime = dateAndTime.toString(),
                        isCompleted = false,
                        groupName = groupName.toString()
                    )
                )
            }
            latestState.value = UIEvent.SaveToDoSuccess("ToDo added")
        } else {
            viewModelScope.launch {
                toDoUseCases.updateToDo(
                    ToDo(
                        id = toDo.id,
                        title = title.toString(),
                        description = description.toString(),
                        dateAndTime = dateAndTime.toString(),
                        isCompleted = toDo.isCompleted,
                        groupName = groupName.toString()
                    )
                )
            }
            latestState.value = UIEvent.SaveToDoSuccess("ToDo updated")
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

    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
        data class SaveToDoSuccess(val message: String) : UIEvent()
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
