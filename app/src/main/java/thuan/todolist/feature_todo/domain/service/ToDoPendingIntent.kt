package thuan.todolist.feature_todo.domain.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import thuan.todolist.feature_todo.domain.service.constants.ACTION_DELETE
import thuan.todolist.feature_todo.domain.service.constants.ACTION_DONE
import thuan.todolist.feature_todo.domain.service.constants.TODO_ID

object ToDoPendingIntent {
    fun getSchedulePendingIntent(context: Context, intent: Intent, id: Int): PendingIntent {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                context,
                id,
                intent,
                //PendingIntent.FLAG_IMMUTABLE is used to prevent the PendingIntent from being updated
                // after it is created
                // PendingInternt.FlAG_UPDATE_CURRENT is used to update the pending intent
                // if it already exists
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getDeletePendingIntent(
        context: Context,
        id: Long
    ): PendingIntent {
        Log.i("ToDoPendingIntent", "getDeletePendingIntent: ")
        val broadcastIntent = Intent(context, ToDoSateReceiver::class.java)
        broadcastIntent.action = ACTION_DELETE
        Log.i("ToDoPendingIntent", "getDeletePendingIntent: $id")
        broadcastIntent.putExtra(TODO_ID, id)
        return PendingIntent.getBroadcast(
            context,
            id.toInt(),
            broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getDonePendingIntent(
        context: Context,
        id: Long
    ):PendingIntent{
        val broadcastIntent = Intent(context, ToDoSateReceiver::class.java)
        broadcastIntent.action = ACTION_DONE
        broadcastIntent.putExtra(TODO_ID, id)
        return PendingIntent.getBroadcast(
            context,
            id.toInt(),
            broadcastIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

}