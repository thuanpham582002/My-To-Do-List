package com.example.todoapp_cleanarch_koin.di

import androidx.lifecycle.SavedStateHandle
import com.example.core.domain.use_case.TodoUseCase
import com.example.core.domain.use_case.TodoUseCaseImpl
import com.example.todoapp_cleanarch_koin.ui.notification.TodoScheduler
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.AddEditViewModel
import com.example.todoapp_cleanarch_koin.ui.todo.addgroup.AddGroupViewModel
import com.example.todoapp_cleanarch_koin.ui.todo.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val useCaseModule = module {
    factory<TodoUseCase> { TodoUseCaseImpl(get()) }
    single {
        SavedStateHandle()
    }
}

val notificationModule = module {
    single { TodoScheduler(androidContext()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { AddEditViewModel(get(), get()) }
    viewModel { AddGroupViewModel(get(), get()) }
}