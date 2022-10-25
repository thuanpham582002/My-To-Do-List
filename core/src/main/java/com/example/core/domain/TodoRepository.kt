package com.example.core.domain

import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.relation.GroupWithTodos
import com.example.core.data.source.local.model.todo.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodo(): Flow<List<TodoEntity>>
    fun getTodoById(id: Long): TodoEntity?
    fun getAllTodoWithGroup(groupName: String): Flow<GroupWithTodos>
    fun getAllGroup(): Flow<List<GroupTodoEntity>>
    fun getGroupById(id: Long): GroupTodoEntity?
    suspend fun deleteTodo(todoEntity: TodoEntity)
    suspend fun insertTodo(todoEntity: TodoEntity): Long
    suspend fun insertGroup(groupTodoEntity: GroupTodoEntity)
    suspend fun updateTodo(todoEntity: TodoEntity)
    suspend fun deleteAllTodo()
    suspend fun deleteAllCompletedTodo()
    suspend fun deleteGroupTodo(groupTodoEntity: GroupTodoEntity)
}