package thuan.todolist.feature_todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_todo_table")
data class GroupToDo(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "group_name")
    val name: String,
)
