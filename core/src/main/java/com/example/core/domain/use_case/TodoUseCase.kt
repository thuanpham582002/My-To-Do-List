package com.example.core.domain.use_case

import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.util.TodoOrder
import kotlinx.coroutines.flow.Flow

interface TodoUseCase {
    fun getAllTodoEntity(order: TodoOrder.Order): Flow<List<TodoEntity>>
    fun getAllGroupTodoEntity(): Flow<List<GroupTodoEntity>>
    suspend fun getTodoById(id: Long): TodoEntity?
    suspend fun getGroupById(id: Long): GroupTodoEntity?
    suspend fun insertTodoEntity(todoEntity: TodoEntity): Long
    suspend fun insertGroupTodoEntity(groupTodoEntity: GroupTodoEntity)
    suspend fun deleteTodoEntity(todoEntity: TodoEntity)
    suspend fun deleteGroupTodoEntity(groupTodoEntity: GroupTodoEntity)
    suspend fun updateTodoEntity(todoEntity: TodoEntity)
    suspend fun updateGroupTodoEntity(groupTodoEntity: GroupTodoEntity)
}