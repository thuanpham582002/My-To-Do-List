package thuan.todolist.feature_todo.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.service.constants.TODO_DESCRIPTION
import thuan.todolist.feature_todo.domain.service.constants.TODO_ID
import thuan.todolist.feature_todo.domain.service.constants.TODO_TITLE

fun toDoScheduleNotification(
    context: Context,
    toDo: ToDo
) {

    if (toDo.dateAndTime == null || toDo.isCompleted)
        return
    val timeTriggerInMillis = toDo.dateAndTime.time
    val broadcastIntent = Intent(
        context, ToDoNotificationReceiver::class.java
    ).apply {
        putExtra(TODO_TITLE, toDo.title)
        putExtra(TODO_DESCRIPTION, toDo.description)
        putExtra(TODO_ID, toDo.id)
    }

    // Setting up AlarmManager
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (timeTriggerInMillis > System.currentTimeMillis()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeTriggerInMillis,
                ToDoPendingIntent.getSchedulePendingIntent(
                    context,
                    broadcastIntent,
                    toDo.id.toInt()
                )
            )
        }
    }
}

fun toDoCancelAlarmManager(context: Context, id: Long) {
    val broadcastIntent = Intent(
        context, ToDoNotificationReceiver::class.java
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

fun toDoDeleteNotification(context: Context, id: Long) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
    notificationManager.cancel(id.toInt())
}
