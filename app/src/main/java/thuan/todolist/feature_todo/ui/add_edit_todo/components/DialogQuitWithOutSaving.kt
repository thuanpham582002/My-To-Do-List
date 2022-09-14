package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.content.Context
import androidx.appcompat.app.AlertDialog
import thuan.todolist.feature_todo.ui.add_edit_todo.AddEditToDoEvent

class DialogQuitWithOutSaving {
    companion object {
        fun show(context: Context, saveToDo: () -> Unit) {
            AlertDialog.Builder(context)
                .setTitle("Quit without saving?")
                .setPositiveButton("Yes") { _, _ ->
                    saveToDo()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}