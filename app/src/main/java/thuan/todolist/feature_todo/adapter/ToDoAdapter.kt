package thuan.todolist.feature_todo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import thuan.todolist.R
import thuan.todolist.databinding.ItemTodoBinding
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.viewmodel.ToDoViewModel


class ToDoAdapter(private val viewModel: ToDoViewModel) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    private var dataList = emptyList<ToDo>()

    inner class ToDoViewHolder(val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            todo: ToDo,
            viewModel: ToDoViewModel
        ) {  // fun bind is used to bind data to the view
            binding.todo = todo
            binding.viewModel = viewModel
            binding.executePendingBindings()
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

        // set animation fade in for each item
//        holder.binding.root.alpha = 0f
//        holder.binding.root.translationY = 100f
//        holder.binding.root.animate().alpha(1f).translationY(0f).setDuration(500).start() gây lỗi khi bind lại
        val animation: Animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.anim_fade_in)
        holder.binding.root.startAnimation(animation)

        holder.binding.root.setOnLongClickListener() {
            Snackbar.make(
                holder.binding.root,
                "Delete ${dataList[position].title}?",
                Snackbar.LENGTH_LONG
            ).setAction("Yes") {
                viewModel.deleteToDo(dataList[position])
            }.show()

            true
        }
    }

    override fun onViewDetachedFromWindow(holder: ToDoViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.root.clearAnimation()

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
