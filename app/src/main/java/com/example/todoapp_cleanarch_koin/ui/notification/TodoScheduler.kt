package com.example.todoapp_cleanarch_koin.ui.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_DESCRIPTION
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_ID
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_TITLE

class TodoScheduler(val context: Context) {
    fun todoScheduleNotification(
        todoEntity: TodoEntity
    ) {
        if (todoEntity.dateAndTime == null || todoEntity.isCompleted) return
        val timeTriggerInMillis = todoEntity.dateAndTime!!.time
        val broadcastIntent = Intent(context, TodoNotificationReceiver::class.java).apply {
            putExtra(TODO_TITLE, todoEntity.title)
            putExtra(TODO_DESCRIPTION, todoEntity.description)
            putExtra(TODO_ID, todoEntity.id)
        }

        // Setting up AlarmManager
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (timeTriggerInMillis > System.currentTimeMillis()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeTriggerInMillis,
                    TodoPendingIntent.getSchedulePendingIntent(
                        context, broadcastIntent, todoEntity.id.toInt()
                    )
                )
            }
        }
    }

    fun todoCancelAlarmManager(id: Long) {
        val broadcastIntent = Intent(
            context, TodoNotificationReceiver::class.java
        )
        val pIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                broadcastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            TODO("VERSION.SDK_INT < M")
        }
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(pIntent)
    }

    fun todoDeleteNotification(id: Long) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.cancel(id.toInt())
    }
}


