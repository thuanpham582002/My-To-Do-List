package thuan.todolist.feature_todo.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection

class ToDoBootCompleteReceiver : BroadcastReceiver() {

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