package com.example.core.data.source.local.model.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.todo.TodoEntity


data class GroupWithTodos(
    @Embedded val group: GroupTodoEntity,  // Embedded annotation is used to tell Room that this is a nested object
    @Relation(                      // Relation annotation is used to tell Room that this is a relation
        parentColumn = "group_id", // parentColumn is used to tell Room that this is the column in the parent table
        entityColumn = "group_id"
    )
    val todos: List<TodoEntity>
)
