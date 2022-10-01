package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogQuitWithOutSaving {
    fun show(context: Context, actionQuit: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(context.resources.getString(thuan.todolist.R.string.are_you_sure_quit_without_saving))
            .setPositiveButton(context.resources.getString(thuan.todolist.R.string.yes)) { _, _ ->
                actionQuit()
            }
            .setNegativeButton(context.resources.getString(thuan.todolist.R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}