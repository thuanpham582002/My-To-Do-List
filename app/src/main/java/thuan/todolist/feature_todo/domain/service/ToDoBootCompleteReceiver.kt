package thuan.todolist.feature_todo.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection

class ToDoBootCompleteReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.i("notification", "onReceive:  BOOT_COMPLETED")
            CoroutineScope(Dispatchers.IO).launch {
                Injection.provideToDoRepository(context).getAllToDoSync().also {
                    Log.i("notification", "onReceive:  boot2")
                    it.forEach { todo ->
                        toDoScheduleNotification(
                            context,
                            todo.id,
                            todo.title,
                            todo.description,
                            todo.dateAndTime
                        )
                    }
                }
            }
        }
    }
}