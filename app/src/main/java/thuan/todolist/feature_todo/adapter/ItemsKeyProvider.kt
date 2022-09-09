package thuan.todolist.feature_todo.adapter

import androidx.recyclerview.selection.ItemKeyProvider

class ItemsKeyProvider(private val adapter: ToDoAdapter) : ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long {
        return adapter.dataList[position].id.toLong()
    }

    override fun getPosition(key: Long): Int {
        return adapter.dataList.indexOfFirst { it.id.toLong() == key }
    }
}