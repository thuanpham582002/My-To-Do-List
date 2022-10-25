package com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.selectiontracker

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.TodoAdapter

class TodosKeyProvider(private val adapter: TodoAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long {
        return adapter.dataList[position].id
    }

    override fun getPosition(key: Long): Int {
        // indexOfFirst returns the first index of the element for which the given predicate is true.
        return adapter.dataList.indexOfFirst { it.id == key }
    }
}