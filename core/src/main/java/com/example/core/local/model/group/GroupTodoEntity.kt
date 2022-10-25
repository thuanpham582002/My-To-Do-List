package com.example.core.data.source.local.model.group

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "group_todo_table")
data class GroupTodoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    val id: Long = 0,
    @ColumnInfo(name = "group_name")
    val name: String = "",
    @ColumnInfo(name = "group_color")
    val color: Int? = null
)

