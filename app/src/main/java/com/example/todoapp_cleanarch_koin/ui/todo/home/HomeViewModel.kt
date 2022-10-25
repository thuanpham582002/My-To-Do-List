package com.example.todoapp_cleanarch_koin.ui.todo.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.use_case.TodoUseCase
import com.example.core.domain.util.TodoOrder
import com.example.todoapp_cleanarch_koin.ui.notification.TodoScheduler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class HomeViewModel(private val todoUseCase: TodoUseCase) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    private val _sateListGroupTodoEntity: MutableStateFlow<List<GroupTodoEntity>> =
        MutableStateFlow(emptyList())
    val stateListGroupTodoEntity: StateFlow<List<GroupTodoEntity>> = _sateListGroupTodoEntity

    private val todoScheduler: TodoScheduler = get(TodoScheduler::class.java)
    private var getTodosJob: Job? = null
    var currentGroupIndex: Int = 0


    init {
        viewModelScope.launch {
            todoUseCase.getAllGroupTodoEntity().collect {
                _sateListGroupTodoEntity.value = it
            }
        }
        getTodos(TodoOrder.Order())
    }

    private fun getTodos(order: TodoOrder.Order) {
        Log.i("HomeViewModel", "getTodos: $order")
        // flow onEach is like a observer
        // flow launchIn is like a lifecycleScope
        // difference between flow onEach and flow collect is that flow onEach is not a suspend function
        getTodosJob?.cancel()
        getTodosJob = todoUseCase.getAllTodoEntity(order).onEach { todos ->
            _state.value = _state.value.copy(
                listTodo = todos,
                todoOrder = order
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.DeleteToDo -> {
                viewModelScope.launch {
                    todoUseCase.deleteTodoEntity(event.todoEntity)
                    todoScheduler.todoCancelAlarmManager(event.todoEntity.id)
                }
            }
            is HomeEvent.Order -> {
                when (event.todoOrder) {
                    is TodoOrder.GroupType -> {
                        getTodos(_state.value.todoOrder.copy(groupType = event.todoOrder))
                    }

                    is TodoOrder.Order -> TODO()
                    is TodoOrder.OrderBy -> {
                        getTodos(_state.value.todoOrder.copy(orderBy = event.todoOrder))
                    }
                    is TodoOrder.OrderType -> {
                        getTodos(_state.value.todoOrder.copy(orderType = event.todoOrder))

                    }
                    is TodoOrder.TodoType -> {
                        getTodos(_state.value.todoOrder.copy(todoType = event.todoOrder))
                    }
                }
            }
            is HomeEvent.ToggleOrderSection -> {
                _state.value =
                    _state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }
            is HomeEvent.UpdateToDo -> {
                viewModelScope.launch {
                    todoUseCase.updateTodoEntity(event.todoEntity)

                }
            }
            is HomeEvent.CurrentGroupIndex -> {
                currentGroupIndex = event.groupIndex
            }
            is HomeEvent.SearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.newText)
            }
            is HomeEvent.SaveGroup -> {
                viewModelScope.launch {
                    todoUseCase.insertGroupTodoEntity(event.group)
                }
            }
        }

    }

    fun saveStateTodo(todoEntity: TodoEntity) {
        val savedStateHandle: SavedStateHandle = get(SavedStateHandle::class.java)
        savedStateHandle["todoEntity"] = todoEntity
    }
}