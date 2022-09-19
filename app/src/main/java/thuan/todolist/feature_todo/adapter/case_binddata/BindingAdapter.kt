package thuan.todolist.feature_todo.adapter.case_binddata

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import com.google.android.material.card.MaterialCardView
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.ui.home.ToDoViewModel
import thuan.todolist.feature_todo.ui.home.ToDosEvent


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
            viewModel.onEvent(ToDosEvent.UpdateToDo(toDo.copy(isCompleted = true)))
        } else {
            viewModel.onEvent(ToDosEvent.UpdateToDo(toDo.copy(isCompleted = false)))
        }
    }
}
