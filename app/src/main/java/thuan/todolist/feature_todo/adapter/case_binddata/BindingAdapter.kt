package thuan.todolist.feature_todo.adapter.case_binddata

import android.util.Log
import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.ui.home.HomeFragment
import thuan.todolist.feature_todo.ui.home.HomeFragmentDirections
import thuan.todolist.feature_todo.ui.home.ToDoViewModel


@BindingAdapter(value = ["todo", "viewModel"])
fun goToEditFragment(materialCardView: MaterialCardView, toDo: ToDo, toDoViewModel: ToDoViewModel) {
    materialCardView.setOnClickListener {
        toDoViewModel.goToAddAndEditToDoFragment(it, toDo)
    }
}

@BindingAdapter(value = ["todo", "vm"])
fun isChecking(checkBox: CheckBox, toDo: ToDo, viewModel: ToDoViewModel) {
    checkBox.setOnCheckedChangeListener(null)
    checkBox.isChecked = toDo.isCompleted
    checkBox.setOnCheckedChangeListener { _, b ->
        if (b) {
            viewModel.updateToDo(
                ToDo(
                    id = toDo.id,
                    title = toDo.title,
                    description = toDo.description,
                    dateAndTime = toDo.dateAndTime,
                    isCompleted = true,
                    groupName = toDo.groupName
                )
            )
        } else {
            viewModel.updateToDo(
                ToDo(
                    id = toDo.id,
                    title = toDo.title,
                    description = toDo.description,
                    dateAndTime = toDo.dateAndTime,
                    isCompleted = false,
                    groupName = toDo.groupName
                )
            )
        }
    }
}
