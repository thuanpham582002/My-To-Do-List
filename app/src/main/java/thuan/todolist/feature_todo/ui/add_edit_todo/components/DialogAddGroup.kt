package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import thuan.todolist.MainActivity

object DialogAddGroup {
    @SuppressLint("InflateParams")
    fun show(context: Context, onAddGroup: (String) -> Unit) {
        AlertDialog.Builder(context).apply {
            val viewInflater = LayoutInflater.from(context).inflate(
                thuan.todolist.R.layout.text_input,
                null
            )
            val editText = viewInflater.findViewById<EditText>(thuan.todolist.R.id.et_input)
            setView(viewInflater)
            setTitle(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.add_group))
            setPositiveButton(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.add)) { _, _ ->
                val groupName = editText.text.toString()
                onAddGroup(groupName)
            }
            setNegativeButton(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }
}