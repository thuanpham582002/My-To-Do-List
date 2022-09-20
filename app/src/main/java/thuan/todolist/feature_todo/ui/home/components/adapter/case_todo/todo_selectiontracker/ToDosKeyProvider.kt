package thuan.todolist.feature_todo.ui.home.components.adapter.case_todo.todo_selectiontracker

import androidx.recyclerview.selection.ItemKeyProvider
import thuan.todolist.feature_todo.ui.home.components.adapter.case_todo.ToDoAdapter

class ToDosKeyProvider(private val adapter: ToDoAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long {
        return adapter.dataList[position].id.toLong()
    }

    override fun getPosition(key: Long): Int {
        // indexOfFirst returns the first index of the element for which the given predicate is true.
        return adapter.dataList.indexOfFirst { it.id.toLong() == key }
    }
}