package thuan.todolist.feature_todo.ui.home.components.adapter.case_todo.util

import androidx.recyclerview.widget.DiffUtil
import thuan.todolist.feature_todo.domain.model.ToDo

class ToDoDiffUtil(
    private val oldList: List<ToDo>,
    private val newList: List<ToDo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].dateAndTime == newList[newItemPosition].dateAndTime
                && oldList[oldItemPosition].isCompleted == newList[newItemPosition].isCompleted
    }
}