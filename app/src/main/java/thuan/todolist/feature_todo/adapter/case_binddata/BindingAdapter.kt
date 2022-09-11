package thuan.todolist.feature_todo.adapter.case_binddata

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import thuan.todolist.R
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.ui.home.HomeFragment
import thuan.todolist.feature_todo.ui.home.HomeFragmentDirections


@BindingAdapter("android:goToEdit")
fun goToEditFragment(materialCardView: MaterialCardView, toDo: ToDo) {
    materialCardView.setOnClickListener {
        it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment())
    }
}