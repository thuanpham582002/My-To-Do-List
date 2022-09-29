package thuan.todolist.feature_todo.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import thuan.todolist.feature_todo.domain.service.constant.TODO_DESCRIPTION
import thuan.todolist.feature_todo.domain.service.constant.TODO_ID
import thuan.todolist.feature_todo.domain.service.constant.TODO_TITLE
import java.text.SimpleDateFormat
import java.util.*

fun toDoScheduleNotification(
    context: Context,
    id: Long,
    title: String,
    description: String,
    date: String
) {

    if (date == "Time not set")
        return
    val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    val timeTriggerInMillis = sdf.parse(date)!!.time
    val broadcastIntent = Intent(
        context, ToDoNotificationReceiver::class.java
    ).apply {
        putExtra(TODO_TITLE, title)
        putExtra(TODO_DESCRIPTION, description)
        putExtra(TODO_ID, id)
    }

    // Setting up AlarmManager
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    if (timeTriggerInMillis > System.currentTimeMillis()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeTriggerInMillis,
                ToDoPendingIntent.getSchedulePendingIntent(context, broadcastIntent, id.toInt())
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
