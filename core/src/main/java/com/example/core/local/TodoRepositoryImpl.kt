package com.example.core.data.source.local

import com.example.core.data.source.local.model.TodoDao
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.relation.GroupWithTodos
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.TodoRepository
import com.example.core.domain.model.group.InvalidGroupException
import com.example.core.domain.model.todo.InvalidTodoException
import kotlinx.coroutines.flow.Flow

class TodoRepositoryImpl(private val todoDao: TodoDao) : TodoRepository {
    override fun getAllTodo(): Flow<List<TodoEntity>> {
        return todoDao.getAllTodo()
    }

    override fun getTodoById(id: Long): TodoEntity? {
        return todoDao.getTodoById(id)
    }

    override fun getAllTodoWithGroup(groupName: String): Flow<GroupWithTodos> {
        return todoDao.getAllTodoWithGroup(groupName)
    }

    override fun getAllGroup(): Flow<List<GroupTodoEntity>> {
        return todoDao.getAllGroup()
    }

    override fun getGroupById(id: Long): GroupTodoEntity? {
        return todoDao.getGroupById(id)
    }

    override suspend fun deleteTodo(todoEntity: TodoEntity) {
        todoDao.deleteTodoById(todoEntity)
    }

    override suspend fun insertTodo(todoEntity: TodoEntity): Long {
        if (todoEntity.title.isEmpty()) {
            throw InvalidTodoException("Title cannot be empty")
        }
        return todoDao.insertTodo(todoEntity)
    }

    override suspend fun updateTodo(todoEntity: TodoEntity) {
        if (todoEntity.title.isEmpty()) {
            throw InvalidTodoException("Title cannot be empty")
        }
        todoDao.updateTodo(todoEntity)
    }

    override suspend fun insertGroup(groupTodoEntity: GroupTodoEntity) {
        if (groupTodoEntity.name.isEmpty()) {
            throw InvalidGroupException("Group name cannot be empty")
        }
        todoDao.insertGroup(groupTodoEntity)
    }



    override suspend fun deleteAllTodo() {
        todoDao.deleteAllTodo()
    }

    override suspend fun deleteAllCompletedTodo() {
        todoDao.deleteAllCompletedTodo()
    }

    override suspend fun deleteGroupTodo(groupTodoEntity: GroupTodoEntity) {
        todoDao.deleteGroup(groupTodoEntity)
    }
}