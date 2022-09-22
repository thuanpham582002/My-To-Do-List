package thuan.todolist.feature_todo.domain.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
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

// Intent to start the Broadcast Receiver
    val broadcastIntent = Intent(
        context, ToDoNotificationReceiver::class.java
    ).apply {
        putExtra(TODO_TITLE, title)
        putExtra(TODO_DESCRIPTION, description)
        putExtra(TODO_ID, id)
    }

    // Date = "15:49 22/08/2022"
    val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    val timeTriggerInMillis =
        sdf.parse(date)!!.time

    val pIntent =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                id.toInt(),
                broadcastIntent,
                //PendingIntent.FLAG_IMMUTABLE is used to prevent the PendingIntent from being updated
                // after it is created
                // PendingInternt.FlAG_UPDATE_CURRENT is used to update the pending intent
                // if it already exists
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            TODO("VERSION.SDK_INT < M")
        }

    // Setting up AlarmManager
    val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmMgr.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        timeTriggerInMillis,
        pIntent
    )
}

fun toDoCancelNotification(context: Context, id: Long) {
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
