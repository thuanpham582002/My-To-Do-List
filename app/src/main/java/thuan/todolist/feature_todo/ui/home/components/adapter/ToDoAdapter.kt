package thuan.todolist.feature_todo.ui.home.components.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import thuan.todolist.R
import thuan.todolist.databinding.ItemTodoBinding
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.util.ToDoUtils
import thuan.todolist.feature_todo.ui.home.ToDoViewModel
import thuan.todolist.feature_todo.ui.home.ToDosEvent
import thuan.todolist.feature_todo.ui.home.components.adapter.util.ToDoDiffUtil

class ToDoAdapter(private val viewModel: ToDoViewModel) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
    var dataList = emptyList<ToDo>()
    var tracker: SelectionTracker<Long>? = null

    inner class ToDoViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            todoPos: Int,
            viewModel: ToDoViewModel
        ) {  // fun bind is used to bind data to the view
            Log.i("adapter", adapterPosition.toString())
            startAnimation()
            checkItemSelection(dataList[todoPos])
            checkExpired(dataList[todoPos])
            bindData(dataList[todoPos], viewModel)
        }

        private fun bindData(
            todo: ToDo,
            viewModel: ToDoViewModel
        ) {
            binding.todo = todo
            binding.viewModel = viewModel
            binding.tvDateAndTime.text =
                ToDoUtils.dateToString(binding.root.context, todo.dateAndTime)
            binding.executePendingBindings()
        }

        private fun startAnimation() {
            // set animation fade in for each item
//        holder.binding.root.alpha = 0f
//        holder.binding.root.translationY = 100f
//        holder.binding.root.animate().alpha(1f).translationY(0f).setDuration(500).start()
            val animation: Animation =
                AnimationUtils.loadAnimation(binding.root.context, R.anim.anim_fade_in)
            binding.root.startAnimation(animation)
        }

        fun detachAnimation() {
            binding.root.clearAnimation()
        }

        private fun checkItemSelection(todo: ToDo) {
            if (tracker?.isSelected(todo.id) == true) {
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
                    return dataList[adapterPosition].id
                }

                override fun getPosition(): Int {
                    return adapterPosition
                }
            }
        }

        private fun checkExpired(todo: ToDo) {
            if (todo.dateAndTime == null) return

            val dateAndTimeInMilliSeconds = todo.dateAndTime.time
            val currentTime = System.currentTimeMillis()
            if (todo.isExpired != (dateAndTimeInMilliSeconds < currentTime)) {
                viewModel.onEvent(ToDosEvent.UpdateToDo(todo.copy(isExpired = dateAndTimeInMilliSeconds < currentTime)))
                dataList = dataList.toMutableList().apply {
                    set(
                        adapterPosition,
                        todo.copy(isExpired = dateAndTimeInMilliSeconds < currentTime)
                    )
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
        holder.bind(position, viewModel)
        Log.i("adapter", "onBindViewHolder" + dataList[position].id + "size: " + dataList.size)
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
        toDoDiffResult.dispatchUpdatesTo(this)  // dispatchUpdatesTo is used to update the recyclerview
    }
}
