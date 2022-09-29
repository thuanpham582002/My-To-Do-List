package thuan.todolist.feature_todo.domain.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.service.constant.TODO_CHANNEL_ID
import thuan.todolist.feature_todo.domain.service.constant.TODO_DESCRIPTION
import thuan.todolist.feature_todo.domain.service.constant.TODO_ID
import thuan.todolist.feature_todo.domain.service.constant.TODO_TITLE
import thuan.todolist.feature_todo.ui.add_edit_todo.AddAndEditFragmentArgs
import thuan.todolist.feature_todo.ui.home.ToDoViewModel

class ToDoNotificationReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Alarm ToDo"
            val descriptionText = "Alarm details"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(TODO_CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        CoroutineScope(Dispatchers.IO).launch {
            // Generate an Id for each notification
            val id = intent.getLongExtra(TODO_ID, 0)
            val toDo = Injection.provideToDoRepository(context).getToDoById(id)
            Log.i("notification", "onReceive: $id")

            /** Create the pending intent for the notification
             *
            Navigate to the details screen when the notification is clicked

            Pass the id of the toDo as an argument

            Use a NavDeepLink to navigate to the details screen

            This is the same as the action in the navigation graph

            The action is defined in the navigation graph
             */
            val args = AddAndEditFragmentArgs.Builder(toDo).build().toBundle()

            val openEditIntent = NavDeepLinkBuilder(context)
                .setGraph(thuan.todolist.R.navigation.nav_graph)
                .setDestination(thuan.todolist.R.id.addAndEditFragment)
                .setArguments(args)
                .createPendingIntent()

            // Create the notification to be shown
            val mBuilder = NotificationCompat.Builder(context, TODO_CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(intent.getStringExtra(TODO_TITLE))
                .setContentText(intent.getStringExtra(TODO_DESCRIPTION))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(openEditIntent)

            mBuilder.addAction(
                android.R.drawable.ic_menu_delete,
                "Delete Todo",
                ToDoPendingIntent.getDeletePendingIntent(
                    context,
                    id
                )
            )

            mBuilder.addAction(
                thuan.todolist.R.drawable.ic_done,
                "Mark Done",
                ToDoPendingIntent.getDonePendingIntent(
                    context,
                    id
                )
            )

            // Get the Notification manager service
            val am = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // update toDO
            ToDoViewModel.instance?.apply {
                notifiItemPos.postValue(
                    listTodo.value?.indexOf(
                        toDo
                    )
                )
            }
            // Show a notification
            am.notify(id.toInt(), mBuilder.build())
        }
    }
}