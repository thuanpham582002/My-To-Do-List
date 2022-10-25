package com.example.todoapp_cleanarch_koin.ui.todo.addedittodo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.model.todo.InvalidTodoException
import com.example.core.domain.use_case.TodoUseCase
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.ui.notification.TodoScheduler
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.constants.BACK_TO_PREVIOUS_SCREEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class AddEditViewModel(
    private val todoUseCase: TodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val coreTodoEntity = savedStateHandle.get<TodoEntity>("todoEntity")
    private val todoScheduler: TodoScheduler = get(TodoScheduler::class.java)
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _stateFlow = MutableStateFlow(AddEditState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        if (coreTodoEntity != null) _stateFlow.value = _stateFlow.value.copy(
            toDo = coreTodoEntity
        )
    }

    fun onEvent(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.EnteredTitle -> {
                _stateFlow.value = _stateFlow.value.copy(
                    toDo = _stateFlow.value.toDo.copy(
                        title = event.title
                    )
                )
            }
            is AddEditEvent.EnteredDate -> {
                _stateFlow.value = _stateFlow.value.copy(
                    toDo = _stateFlow.value.toDo.copy(
                        dateAndTime = event.date
                    )
                )
            }
            is AddEditEvent.EnteredColor -> {
                Log.i("AddEdit", "onEvent: ${event.color}")
                _stateFlow.value = _stateFlow.value.copy(
                    toDo = _stateFlow.value.toDo.copy(
                        color = event.color
                    )
                )
                Log.i("AddEdit", "onEvent: ${event.color}")

            }
            is AddEditEvent.SaveToDo -> {
                saveToDo()
            }
        }
    }

    private fun saveToDo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (todoUseCase.getTodoById(stateFlow.value.toDo.id) == null) {
                    Log.i("AddEditViewmodel", "insert: ${stateFlow.value.toDo}")
                    todoUseCase.insertTodoEntity(stateFlow.value.toDo)
                } else {
                    Log.i("AddEditViewmodel", "update: ${stateFlow.value.toDo}")
                    todoUseCase.updateTodoEntity(stateFlow.value.toDo)
                }
                _eventFlow.emit(UiEvent.ChangeUi(BACK_TO_PREVIOUS_SCREEN, R.string.todo_saved))
                Log.i("AddEditViewmodel", "saveToDo: ${stateFlow.value.toDo}")
                todoScheduler.todoScheduleNotification(stateFlow.value.toDo)
            } catch (e: InvalidTodoException) {
                _eventFlow.emit(UiEvent.Message(R.string.invalid_todo))
            } catch (e: Exception) {
                Log.i("AddEditViewmodel", "saveToDo: ${e.message}")
                _eventFlow.emit(UiEvent.Message(R.string.todo_not_saved))
            }
        }
    }

    sealed interface UiEvent {
        data class Message(val idMessage: Int) : UiEvent
        data class ChangeUi(val id: Int, val idMessage: Int) : UiEvent
    }
}