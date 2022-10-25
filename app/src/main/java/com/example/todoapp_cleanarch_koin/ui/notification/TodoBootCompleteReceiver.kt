package com.example.todoapp_cleanarch_koin.ui.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.core.domain.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class TodoBootCompleteReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        val todoScheduler: TodoScheduler = get(TodoScheduler::class.java)

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.i("notification", "onReceive:  BOOT_COMPLETED")
            CoroutineScope(Dispatchers.IO).launch {
                val todoRepository: TodoRepository = get(TodoRepository::class.java)
                todoRepository.getAllTodo().onEach {
                    Log.i("notification", "onReceive:  boot2")
                    it.forEach { todo ->
                        todoScheduler.todoScheduleNotification(todo)
                    }
                }
            }
        }
    }
}