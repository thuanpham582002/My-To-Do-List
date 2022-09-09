package thuan.todolist.feature_todo.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class ItemsDetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return (view?.let { recyclerView.getChildViewHolder(it) } as? ToDoAdapter.ToDoViewHolder)?.getItemDetails()
    }
}