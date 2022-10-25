package com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.selectiontracker

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.TodoAdapter

class TodosDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return (view?.let { recyclerView.getChildViewHolder(it) } as? TodoAdapter.TodoViewHolder)?.getToDoDetails()
    }
}