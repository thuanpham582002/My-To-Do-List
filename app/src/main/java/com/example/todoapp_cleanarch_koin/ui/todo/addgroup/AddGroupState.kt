package com.example.todoapp_cleanarch_koin.ui.todo.addgroup

import com.example.core.data.source.local.model.group.GroupTodoEntity

data class AddGroupState(
    val groupTodoEntity: GroupTodoEntity = GroupTodoEntity()
)