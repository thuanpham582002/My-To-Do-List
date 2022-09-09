package thuan.todolist.feature_todo.adapter

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import thuan.todolist.R

@BindingAdapter("android:go_to_add_todo")
fun goToAddTodo(fab: FloatingActionButton, navigate: Boolean) {
    fab.setOnClickListener {
        fab.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
    }
}