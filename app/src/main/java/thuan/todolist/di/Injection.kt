package thuan.todolist.di

import android.content.Context
import kotlinx.coroutines.Dispatchers
import thuan.todolist.feature_todo.data.data_source.ToDoDatabase
import thuan.todolist.feature_todo.data.repository.ToDoRepository
import thuan.todolist.feature_todo.data.repository.ToDoRepositoryImpl
import thuan.todolist.feature_todo.domain.use_case.*
import kotlin.coroutines.CoroutineContext

object Injection {


    fun provideToDoRepository(context: Context): ToDoRepositoryImpl {
        val db = ToDoDatabase.getInstance(context)
        return ToDoRepositoryImpl(db.toDoDao)
    }

    fun provideToDoUseCases(repository: ToDoRepository): ToDoUseCases {
        return ToDoUseCases(
            getToDos = GetToDos(repository),
            getGroupsToDo = GetGroupsToDo(repository),
            addToDo = AddToDo(repository),
            addGroup = AddGroup(repository),
            updateToDo = UpdateToDo(repository),
            deleteToDo = DeleteToDo(repository)
        )
    }

    fun provideIODispatcher(): CoroutineContext {
        return Dispatchers.IO
    }
}