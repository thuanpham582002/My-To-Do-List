package com.example.core.data.source.local.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.todo.TodoEntity

@Database(
    entities = [TodoEntity::class, GroupTodoEntity::class],
    version = 1,
    exportSchema = true
)  // exportSchema = true -> export database to json file
/**
 * class TodoDatabase is used to create database
 */
@TypeConverters(DateConverters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract val todoDao: TodoDao

    companion object {
        //        @Volatile // volatile is used to make sure that the value of INSTANCE is always up-to-date and the same to all execution threads
//        private var instance: TodoDatabase? = null
//        fun getInstance(context: Context): TodoDatabase {
//            return instance ?: synchronized(this) {
//                return Room.databaseBuilder(
//                    context,
//                    TodoDatabase::class.java,
//                    DATABASE_NAME
//                )
//                    .addTypeConverter(DateConverters())
//                    .fallbackToDestructiveMigration().build()
//            }
//        }
        const val DATABASE_NAME = "todo_db"

    }
}