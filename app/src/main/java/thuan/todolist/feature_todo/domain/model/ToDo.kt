package thuan.todolist.feature_todo.domain.model

import android.content.ClipDescription
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "todo_title")
    val title: String,
    @ColumnInfo(name = "todo_description")
    val description: String,
    @ColumnInfo(name = "todo_date_and_time")
    val dateAndTime: String,
    @ColumnInfo(name = "todo_is_completed")
    val isCompleted: Boolean,
    @ColumnInfo(name = "group_name")
    val groupName: String
)
