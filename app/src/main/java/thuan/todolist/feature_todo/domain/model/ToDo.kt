package thuan.todolist.feature_todo.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "todo_title")
    val title: String,
    @ColumnInfo(name = "todo_description")
    val description: String,
    @ColumnInfo(name = "todo_date_and_time")
    val dateAndTime: String,
    @ColumnInfo(name = "todo_is_completed")
    val isCompleted: Boolean,
    val isExpired: Boolean,
    @ColumnInfo(name = "group_name")
    val groupName: String
) : Parcelable

class InvalidToDoException(message: String) : Exception(message)
