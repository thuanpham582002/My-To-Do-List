package com.example.core.domain.util

import com.example.core.data.source.local.model.todo.TodoEntity

sealed interface TodoOrder {
    sealed interface OrderType : TodoOrder {
        object Ascending : OrderType
        object Descending : OrderType

        suspend fun filter(list: List<TodoEntity>, orderType: OrderType): List<TodoEntity> {
            return when (orderType) {
                is Ascending -> list
                is Descending -> list.reversed()
            }
        }
    }

    sealed interface OrderBy : TodoOrder {
        object None : OrderBy
        object Title : OrderBy
        object Date : OrderBy

        suspend fun filter(list: List<TodoEntity>, orderBy: OrderBy): List<TodoEntity> {
            return when (orderBy) {
                is None -> list
                is Title -> list.sortedBy { it.title }
                is Date -> list.sortedBy { it.dateAndTime }
            }
        }
    }

    sealed interface TodoType: TodoOrder {
        object All: TodoType
        object Completed: TodoType
        object Uncompleted: TodoType

        suspend fun filter(list: List<TodoEntity>, todoType: TodoType): List<TodoEntity> {
            return when (todoType) {
                is All -> list
                is Completed -> list.filter { it.isCompleted }
                is Uncompleted -> list.filter { !it.isCompleted }
            }
        }
    }

    sealed interface GroupType : TodoOrder {
        object All : GroupType
        object Default : GroupType
        data class Custom(val groupId: Long) : GroupType

        suspend fun filter(list: List<TodoEntity>, groupType: GroupType): List<TodoEntity> {
            return when (groupType) {
                is All -> list
                is Default -> list.filter { it.groupId == null }
                is Custom -> list.filter { it.groupId == groupType.groupId }
            }
        }
    }

    data class Order(
        val orderType: OrderType = OrderType.Ascending,
        val orderBy: OrderBy = OrderBy.None,
        val groupType: GroupType = GroupType.All,
        val todoType: TodoType = TodoType.All
    ) : TodoOrder
}