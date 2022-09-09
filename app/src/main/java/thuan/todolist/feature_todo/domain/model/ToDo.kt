package thuan.todolist.feature_todo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "todo_title")
    val title: String,
    @ColumnInfo(name = "todo_date")
    val date: String,
    @ColumnInfo(name = "todo_time")
    val time: String,
    @ColumnInfo(name = "todo_is_completed")
    val isCompleted: Boolean,
    @ColumnInfo(name = "group_name")
    val groupName : String
)
