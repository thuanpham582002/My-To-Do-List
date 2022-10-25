package com.example.todoapp_cleanarch_koin.ui.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavDeepLinkBuilder
import com.example.core.domain.TodoRepository
import com.example.todoapp_cleanarch_koin.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_CHANNEL_ID
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_DESCRIPTION
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_ID
import com.example.todoapp_cleanarch_koin.ui.notification.constants.TODO_TITLE
import com.example.todoapp_cleanarch_koin.ui.notification.utils.NotificationUtils
import org.koin.java.KoinJavaComponent.get

class TodoNotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {
            // Generate an Id for each notification
            val id = intent.getLongExtra(TODO_ID, 0)
            val todoRepository: TodoRepository = get(TodoRepository::class.java)
            val todoEntity = todoRepository.getTodoById(id)
            Log.i("notification", "onReceive: $id")

            /** Create the pending intent for the notification
             *
            Navigate to the details screen when the notification is clicked

            Pass the id of the toDo as an argument

            Use a NavDeepLink to navigate to the details screen

            This is the same as the action in the navigation graph

            The action is defined in the navigation graph
             */
            val savedStateHandle: SavedStateHandle = get(SavedStateHandle::class.java)
            savedStateHandle["todoEntity"] = todoEntity
//
            val openEditIntent = NavDeepLinkBuilder(context).setGraph(R.navigation.nav_graph)
                .setDestination(R.id.homeFragment).createPendingIntent()

            // Create the notification to be shown
            val mBuilder = NotificationCompat.Builder(context, TODO_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(intent.getStringExtra(TODO_TITLE))
                .setContentText(intent.getStringExtra(TODO_DESCRIPTION)).setAutoCancel(true)
                .setContentIntent(openEditIntent)

            mBuilder.addAction(
                android.R.drawable.ic_menu_delete,
                NotificationUtils.getStringByLocal(context, R.string.delete),
                TodoPendingIntent.getDeletePendingIntent(
                    context, id
                )
            )

            mBuilder.addAction(
                R.drawable.ic_done,
                NotificationUtils.getStringByLocal(context, R.string.mark_done),
                TodoPendingIntent.getDonePendingIntent(
                    context, id
                )
            )

            // Get the Notification manager service
            val am = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // update to DO
            // Show a notification
            am.notify(id.toInt(), mBuilder.build())
        }
    }
}

