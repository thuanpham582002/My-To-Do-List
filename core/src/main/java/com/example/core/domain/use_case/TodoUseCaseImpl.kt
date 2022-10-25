package com.example.core.domain.use_case

import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.TodoRepository
import com.example.core.domain.util.TodoOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoUseCaseImpl(private val todoRepository: TodoRepository) : TodoUseCase {
    override fun getAllTodoEntity(order: TodoOrder.Order): Flow<List<TodoEntity>> {
        return todoRepository.getAllTodo().map {
            var listTodoEntity = it
            listTodoEntity = order.groupType.filter(listTodoEntity, order.groupType)
            listTodoEntity = order.todoType.filter(listTodoEntity, order.todoType)
            listTodoEntity = order.orderBy.filter(listTodoEntity, order.orderBy)
            listTodoEntity = order.orderType.filter(listTodoEntity, order.orderType)
            listTodoEntity
        }
    }

    override fun getAllGroupTodoEntity(): Flow<List<GroupTodoEntity>> {
        return todoRepository.getAllGroup()
    }

    override suspend fun getTodoById(id: Long): TodoEntity? {
        return todoRepository.getTodoById(id)
    }

    override suspend fun getGroupById(id: Long): GroupTodoEntity? {
        return todoRepository.getGroupById(id)
    }

    override suspend fun insertTodoEntity(todoEntity: TodoEntity): Long {
        return todoRepository.insertTodo(todoEntity)
    }

    override suspend fun insertGroupTodoEntity(groupTodoEntity: GroupTodoEntity) {
        todoRepository.insertGroup(groupTodoEntity)
    }

    override suspend fun deleteTodoEntity(todoEntity: TodoEntity) {
        todoRepository.deleteTodo(todoEntity)
    }

    override suspend fun deleteGroupTodoEntity(groupTodoEntity: GroupTodoEntity) {
        todoRepository.deleteGroupTodo(groupTodoEntity)
    }

    override suspend fun updateTodoEntity(todoEntity: TodoEntity) {
        todoRepository.updateTodo(todoEntity)
    }

    override suspend fun updateGroupTodoEntity(groupTodoEntity: GroupTodoEntity) {
        todoRepository.insertGroup(groupTodoEntity)
    }
}