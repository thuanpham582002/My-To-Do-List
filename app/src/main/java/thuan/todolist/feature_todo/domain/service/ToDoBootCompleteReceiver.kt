package thuan.todolist.feature_todo.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection

class ToDoBootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
//            toDoScheduleNotification(
//                context,
//                555,
//                "xin chao",
//                "xin chao",
//                "19:11 22/09/2022"
//            )
            Log.i("notification", "onReceive:  boot")
            CoroutineScope(Injection.provideIODispatcher()).launch {
                Injection.provideToDoRepository(context).getAllToDoSync().also {
                    Log.i("notification", "onReceive:  boot2")
                    it.forEach { todo ->
                        toDoScheduleNotification(
                            context,
                            todo.id.toLong(),
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