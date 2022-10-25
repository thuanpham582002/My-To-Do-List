package com.example.todoapp_cleanarch_koin.ui.todo.home.components.util

import androidx.recyclerview.widget.DiffUtil
import com.example.core.data.source.local.model.todo.TodoEntity

class TodoRecycleDiffUtil(
    private val oldList: List<TodoEntity>,
    private val newList: List<TodoEntity>
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
        return oldList[oldItemPosition] == newList[newItemPosition] &&
                oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].isCompleted == newList[newItemPosition].isCompleted
                && oldList[oldItemPosition].dateAndTime == newList[newItemPosition].dateAndTime
                && oldList[oldItemPosition].groupId == newList[newItemPosition].groupId
                && oldList[oldItemPosition].color == newList[newItemPosition].color
    }
}