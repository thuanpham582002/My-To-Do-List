package thuan.todolist.feature_todo.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

const val TODO_TITLE = "todo_title"
const val TODO_CHANNEL_ID = "channel_id"
const val TODO_DESCRIPTION = "todo_message"
const val TODO_ID = "todo_id"

class ToDoNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Alarm ToDo"
            val descriptionText = "Alarm details"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(TODO_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        // Create the notification to be shown
        val mBuilder = NotificationCompat.Builder(context!!, TODO_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(intent?.getStringExtra(TODO_TITLE))
            .setContentText(intent?.getStringExtra(TODO_DESCRIPTION))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Get the Notification manager service
        val am = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Generate an Id for each notification
        val id = intent?.getLongExtra(TODO_ID, 0)?.toInt() ?: 0
        Log.i("notification", "onReceive: $id")

        // Show a notification
        am.notify(id, mBuilder.build())
    }
}