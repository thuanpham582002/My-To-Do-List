package thuan.todolist.feature_todo.ui.add_edit_todo

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.InvalidGroupException
import thuan.todolist.feature_todo.domain.model.InvalidToDoException
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.use_case.ToDoUseCases
import thuan.todolist.feature_todo.domain.util.ToDoUtils
import java.util.*

class AddEditToDoViewModel(private val toDoUseCases: ToDoUseCases, private val toDo: ToDo?) :
    ViewModel() {

    val latestState = MutableLiveData<AddAndEditState>()
    private val toDoId = MutableLiveData<Long>()
    val todoTitle = MutableLiveData("")
    val todoDescription = MutableLiveData("")
    val todoDateAndTime: MutableLiveData<Date?> = MutableLiveData()
    val isTimeSet: MutableLiveData<Boolean> = MutableLiveData()
    val isDone: MutableLiveData<Boolean> = MutableLiveData()
    val groupName = MutableLiveData("Default")

    init {
        toDoId.value = toDo?.id
        todoTitle.value = toDo?.title
        todoDescription.value = toDo?.description
        todoDateAndTime.value = toDo?.dateAndTime
        isTimeSet.value = toDo?.dateAndTime != null
        isDone.value = toDo?.isCompleted
        groupName.value = toDo?.groupName
    }

    fun getCurrentToDo(): ToDo {
        return ToDo(
            id = toDo?.id ?: 0,
            title = todoTitle.value.toString(),
            description = todoDescription.value.toString(),
            dateAndTime = todoDateAndTime.value,
            isCompleted = isDone.value ?: false,
            groupName = groupName.value.toString(),
            isExpired = toDo?.isExpired ?: false
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

            is AddEditToDoEvent.EnteredIsDone -> {
                isDone.value = event.isCompleted
            }

            is AddEditToDoEvent.EnteredGroupName -> {
                groupName.value = event.groupName
            }

            is AddEditToDoEvent.SaveGroup -> {
                viewModelScope.launch {
                    try {
                        toDoUseCases.addGroup(GroupToDo(name = event.groupName))
                        latestState.value = AddAndEditState.GroupSaved
                    } catch (e: InvalidGroupException) {
                        latestState.value = AddAndEditState.GroupNotSaved
                    }
                }
            }

            is AddEditToDoEvent.SaveToDo -> {
                saveToDo()
            }
            AddEditToDoEvent.DeleteToDo -> {
                viewModelScope.launch {
                    toDoUseCases.deleteToDo(toDo!!)
                    latestState.value = AddAndEditState.ToDoDeleted
                }
            }
        }
    }

    private fun saveToDo() {
        val title = todoTitle.value
        val description = todoDescription.value
        val dateAndTime = todoDateAndTime.value
        val groupName = groupName.value
        Log.i(TAG, "saveToDo: $title, $description, $dateAndTime, $groupName")
        val isDone = isDone.value

        if (toDo!!.id == -1L) {
            viewModelScope.launch {
                try {
                    toDoId.value = toDoUseCases.addToDo(
                        ToDo(
                            id = 0,
                            title = title.toString(),
                            description = description.toString(),
                            dateAndTime = dateAndTime,
                            isCompleted = isDone ?: false,
                            groupName = groupName.toString(),
                            isExpired = ToDoUtils.isExpired(dateAndTime)
                        )
                    )
                    latestState.value = AddAndEditState.ToDoSaved
                } catch (e: InvalidToDoException) {
                    latestState.value = AddAndEditState.ToDoNotSaved
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
                            dateAndTime = dateAndTime,
                            isCompleted = isDone ?: false,
                            groupName = groupName.toString(),
                            isExpired = ToDoUtils.isExpired(dateAndTime)
                        )
                    )
                    latestState.value = AddAndEditState.ToDoUpdated
                } catch (e: InvalidToDoException) {
                    latestState.value = AddAndEditState.ToDoNotUpdated
                }
            }
        }
    }

    fun getGroupToDo(): LiveData<List<GroupToDo>> {
        return toDoUseCases.getGroupsToDo()
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
