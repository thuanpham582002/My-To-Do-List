package thuan.todolist.feature_todo.adapter.case_todo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import thuan.todolist.R
import thuan.todolist.databinding.ItemTodoBinding
import thuan.todolist.feature_todo.adapter.case_todo.util.ToDoDiffUtil
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.viewmodel.ToDoViewModel


class ToDoAdapter(private val viewModel: ToDoViewModel) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    var dataList = emptyList<ToDo>()
    var tracker: SelectionTracker<Long>? = null

    inner class ToDoViewHolder(val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            todo: ToDo,
            viewModel: ToDoViewModel
        ) {  // fun bind is used to bind data to the view
            Log.i("adapter", adapterPosition.toString())
            bindData(todo, viewModel)
            startAnimation()
            checkItemSelection(todo)

        }

        private fun bindData(
            todo: ToDo,
            viewModel: ToDoViewModel
        ) {
            binding.todo = todo
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        private fun startAnimation() {
            // set animation fade in for each item
//        holder.binding.root.alpha = 0f
//        holder.binding.root.translationY = 100f
//        holder.binding.root.animate().alpha(1f).translationY(0f).setDuration(500).start() gây lỗi khi bind lại
            val animation: Animation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.anim_fade_in)
            binding.root.startAnimation(animation)
        }

        fun detachAnimation() {
            binding.root.clearAnimation()
        }

        private fun checkItemSelection(todo: ToDo) {
            if (tracker?.isSelected(todo.id.toLong()) == true) {
                Log.i("adapter", "checkItemSelection: ")
                binding.root.alpha = 0.5f
            } else {
                binding.root.alpha = 1f
            }
        }

        // fun getItemDetails is used to get the item details
        // which is used by the selection tracker
        // to track the selected items
        // and perform actions on them.
        fun getToDoDetails(): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getSelectionKey(): Long {
                    return dataList[adapterPosition].id.toLong()
                }

                override fun getPosition(): Int {
                    return adapterPosition
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding: ItemTodoBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_todo,
            parent,
            false
        )
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.bind(dataList[position], viewModel)
        Log.i("adapter", "onBindViewHolder" + dataList[position].id)
    }

    override fun onViewDetachedFromWindow(holder: ToDoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.detachAnimation()

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDo>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)  // dispathUpdatesTo is used to update the recyclerview
    }
}
