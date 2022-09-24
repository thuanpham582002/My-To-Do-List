package thuan.todolist.feature_todo.data.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.ToDo


@Database(
    entities = [ToDo::class, GroupToDo::class],
    version = 1,
    exportSchema = true
)  // exportSchema = true -> export database to json file
/**
 * class ToDoDatabase is used to create database
 */
abstract class ToDoDatabase : RoomDatabase() {
    abstract val toDoDao: ToDoDao

    companion object {
        private const val DATABASE_NAME = "todo_db"

        @Volatile // volatile is used to make sure that the value of INSTANCE is always up-to-date and the same to all execution threads
        private var instance: ToDoDatabase? = null
        fun getInstance(context: Context): ToDoDatabase {
            return instance ?: synchronized(this) {
                return Room.databaseBuilder(
                    context,
                    ToDoDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration().build()
            }
        }
    }
}