package thuan.todolist.feature_todo.ui.home.components.adapter

import android.graphics.Color
import android.util.Log
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.service.toDoCancelAlarmManager
import thuan.todolist.feature_todo.domain.service.toDoScheduleNotification
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
            toDoCancelAlarmManager(checkBox.context, toDo.id)
        } else {
            Log.i("notification", " toDo.id.toLong() ${toDo.id}")
            viewModel.onEvent(ToDosEvent.UpdateToDo(toDo.copy(isCompleted = false)))
            toDoScheduleNotification(
                checkBox.context,
                toDo
            )
        }
    }
}

@BindingAdapter(value = ["isCompleted", "isExpired"])
fun isStriked(textView: TextView, isCompleted: Boolean, isExpired: Boolean) {
    if (isCompleted) {
        textView.setTextColor(Color.GRAY)
    } else if (isExpired) {
        textView.setTextColor(Color.RED)
    } else {
        // check if night mode is on
        AppCompatDelegate.getDefaultNightMode().let { mode ->
            if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setTextColor(Color.BLACK)
            }
        }
    }
}
