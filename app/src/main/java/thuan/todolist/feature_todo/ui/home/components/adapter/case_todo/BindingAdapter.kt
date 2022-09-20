package thuan.todolist.feature_todo.ui.home.components.adapter.case_todo

import android.widget.CheckBox
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.ui.home.HomeFragmentDirections
import thuan.todolist.feature_todo.ui.home.ToDoViewModel
import thuan.todolist.feature_todo.ui.home.ToDosEvent


@BindingAdapter(value = ["todo"])
fun goToEditFragment(materialCardView: MaterialCardView, toDo: ToDo) {
    materialCardView.setOnClickListener {
        it.findNavController()
            .navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment(toDo))
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
