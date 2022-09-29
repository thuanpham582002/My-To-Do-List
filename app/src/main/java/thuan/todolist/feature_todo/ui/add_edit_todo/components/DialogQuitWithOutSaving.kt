package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogQuitWithOutSaving {
    fun show(context: Context, actionQuit: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Quit without saving?")
            .setPositiveButton("Yes") { _, _ ->
                actionQuit()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}