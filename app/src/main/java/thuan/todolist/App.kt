package thuan.todolist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import thuan.todolist.feature_todo.domain.service.constants.TODO_CHANNEL_ID

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val name = "Alarm ToDo"
        val descriptionText = "Alarm details"
        val mChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(TODO_CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)
        } else {
            return
        }
        mChannel.description = descriptionText
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}