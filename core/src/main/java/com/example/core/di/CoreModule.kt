package com.example.core.di

import androidx.room.Room
import com.example.core.data.source.local.TodoRepositoryImpl
import com.example.core.data.source.local.model.DateConverters
import com.example.core.data.source.local.model.TodoDatabase
import com.example.core.domain.TodoRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    factory { get<TodoDatabase>().todoDao }
    single {
        Room.databaseBuilder(
            androidContext(),
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME
        )
            .addTypeConverter(DateConverters())
            .fallbackToDestructiveMigration().build()
    }
}

val repositoryModule = module {
    single<TodoRepository> {
        TodoRepositoryImpl(get())
    }
}
