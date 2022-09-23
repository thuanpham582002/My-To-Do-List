package thuan.todolist.feature_todo.ui.home.components.adapter.todo_selectiontracker

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import thuan.todolist.feature_todo.ui.home.components.adapter.ToDoAdapter

class ToDosDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return (view?.let { recyclerView.getChildViewHolder(it) } as? ToDoAdapter.ToDoViewHolder)?.getToDoDetails()
    }
}