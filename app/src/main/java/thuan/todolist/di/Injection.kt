package thuan.todolist.di

import android.content.Context
import thuan.todolist.feature_todo.data.data_source.ToDoDatabase
import thuan.todolist.feature_todo.data.repository.ToDoRepositoryImpl

object Injection {
    fun provideToDoRepository(context: Context): ToDoRepositoryImpl {
        val db = ToDoDatabase.getInstance(context)
        return ToDoRepositoryImpl(db.toDoDao)
    }
}