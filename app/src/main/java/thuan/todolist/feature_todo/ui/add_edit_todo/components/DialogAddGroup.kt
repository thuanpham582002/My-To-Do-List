package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

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
            setTitle("Add Group")
            setPositiveButton("Add") { _, _ ->
                val groupName = editText.text.toString()
                if (groupName.isEmpty()) {
                    Toast.makeText(context, "Group name is empty", Toast.LENGTH_SHORT)
                        .show()
                    return@setPositiveButton
                }
                onAddGroup(groupName)
                Toast.makeText(context, "Add group successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        }.create().show()
    }
}