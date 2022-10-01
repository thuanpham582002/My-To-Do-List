package thuan.todolist

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import thuan.todolist.feature_setting.constants.APP_THEME_MODE
import thuan.todolist.feature_todo.domain.service.constants.TODO_CHANNEL_ID

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val isNightMode = PreferenceManager.getDefaultSharedPreferences(this)
            .getString(APP_THEME_MODE, "light")
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES && isNightMode == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO && isNightMode == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
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