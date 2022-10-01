package thuan.todolist.feature_todo.ui.add_edit_todo.components

import android.content.Context
import androidx.appcompat.app.AlertDialog
import thuan.todolist.MainActivity

object DialogQuitWithOutSaving {
    fun show(context: Context, actionQuit: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.are_you_sure_quit_without_saving))
            .setPositiveButton(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.yes)) { _, _ ->
                actionQuit()
            }
            .setNegativeButton(MainActivity.RESOURCES_INSTANCE.getString(thuan.todolist.R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}