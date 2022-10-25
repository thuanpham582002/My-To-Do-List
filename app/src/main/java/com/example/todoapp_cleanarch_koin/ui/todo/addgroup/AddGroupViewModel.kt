package com.example.todoapp_cleanarch_koin.ui.todo.addgroup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.domain.model.group.InvalidGroupException
import com.example.core.domain.use_case.TodoUseCase
import com.example.todoapp_cleanarch_koin.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddGroupViewModel(
    private val todoUseCase: TodoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _stateFlow = MutableStateFlow(AddGroupState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        savedStateHandle.get<GroupTodoEntity>("groupName")?.let {
            _stateFlow.value = _stateFlow.value.copy(
                groupTodoEntity = it
            )
        }
    }

    fun onEvent(event: AddGroupEvent) {
        when (event) {
            is AddGroupEvent.SaveGroup -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        if (todoUseCase.getGroupById(_stateFlow.value.groupTodoEntity.id) == null) {
                            todoUseCase.insertGroupTodoEntity(_stateFlow.value.groupTodoEntity)
                            _eventFlow.emit(UIEvent.NavigateBack)
                        } else {
                            todoUseCase.updateGroupTodoEntity(_stateFlow.value.groupTodoEntity)
                            _eventFlow.emit(UIEvent.NavigateBack)
                        }
                    } catch (e: InvalidGroupException) {
                        _eventFlow.emit(UIEvent.ShowMessage(R.string.invalid_group))
                    } catch (e: Exception) {
                        _eventFlow.emit(UIEvent.ShowMessage(R.string.group_not_saved))
                    }
                }
            }
            is AddGroupEvent.SelectColor -> {
                _stateFlow.value = _stateFlow.value.copy(
                    groupTodoEntity = _stateFlow.value.groupTodoEntity.copy(
                        color = event.color
                    )
                )
            }
            is AddGroupEvent.EnteredGroupName -> {
                _stateFlow.value = _stateFlow.value.copy(
                    groupTodoEntity = _stateFlow.value.groupTodoEntity.copy(
                        name = event.name
                    )
                )
            }
        }
    }

    sealed interface UIEvent {
        object NavigateBack : AddGroupViewModel.UIEvent
        data class ShowMessage(val idMessage: Int) : UIEvent
        data class ChangeScreen(val screen: Int) : UIEvent
    }
}