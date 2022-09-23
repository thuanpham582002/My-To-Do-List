package thuan.todolist.feature_todo.ui.add_edit_todo

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.InvalidGroupException
import thuan.todolist.feature_todo.domain.model.InvalidToDoException
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.use_case.ToDoUseCases

class AddEditToDoViewModel(private val toDoUseCases: ToDoUseCases, private val toDo: ToDo?) :
    ViewModel() {


    val latestState = MutableLiveData<UIEvent>()
    val toDoId = MutableLiveData<Long>()

    val todoTitle = MutableLiveData("")

    val todoDescription = MutableLiveData("")

    val todoDateAndTime = MutableLiveData("Date not set")
    val isTimeSet = MutableLiveData(false)

    val groupName = MutableLiveData("Default")

    init {
        toDoId.value = toDo?.id
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
                    try {
                        toDoUseCases.addGroup(GroupToDo(name = event.groupName))
                        latestState.value = UIEvent.ShowSnackBar("Group added")
                    } catch (e: InvalidGroupException) {
                        latestState.value = UIEvent.ShowSnackBar(e.message ?: "Cannot save group")
                    }
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

        if (toDo!!.id == -1L) {
            viewModelScope.launch {
                try {
                    toDoId.value = toDoUseCases.addToDo(
                        ToDo(
                            id = 0,
                            title = title.toString(),
                            description = description.toString(),
                            dateAndTime = dateAndTime.toString(),
                            isCompleted = false,
                            groupName = groupName.toString()
                        )
                    )

                    latestState.value = UIEvent.SaveToDoSuccess("ToDo added")
                } catch (e: InvalidToDoException) {
                    latestState.value = UIEvent.ShowSnackBar(e.message ?: "Cannot save todo")
                }
            }
        } else {
            viewModelScope.launch {
                try {
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
                    latestState.value = UIEvent.SaveToDoSuccess("ToDo updated")
                } catch (e: InvalidToDoException) {
                    latestState.value = UIEvent.ShowSnackBar(e.message ?: "Cannot save todo")
                }
            }
        }
    }

    fun getGroupToDo(): LiveData<List<GroupToDo>> {
        return toDoUseCases.getGroupsToDo()
    }

    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
        data class SaveToDoSuccess(val message: String) : UIEvent()
        object None : UIEvent()
    }

}

@Suppress("UNCHECKED_CAST")
class AddEditToDoViewModelFactory(
    private val toDoUseCases: ToDoUseCases,
    private val toDo: ToDo?
) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        fun getInstance(
            toDoUseCases: ToDoUseCases,
            toDo: ToDo?
        ): AddEditToDoViewModelFactory =
            synchronized(this) {   // synchronized: only one thread can access this block of code at a time
                AddEditToDoViewModelFactory(toDoUseCases, toDo)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddEditToDoViewModel(toDoUseCases, toDo) as T
    }
}
