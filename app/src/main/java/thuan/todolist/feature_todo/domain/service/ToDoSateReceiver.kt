package thuan.todolist.feature_todo.domain.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.service.constant.ACTION_DELETE
import thuan.todolist.feature_todo.domain.service.constant.ACTION_DONE
import thuan.todolist.feature_todo.domain.service.constant.TODO_ID
import thuan.todolist.feature_todo.ui.home.ToDoViewModel

class ToDoSateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val id = intent.getLongExtra(TODO_ID, -1)
        if (ACTION_DONE == action) {
            Log.i("ToDoChangeDoneReceiver", "onReceive: $action")
            CoroutineScope(Dispatchers.IO).launch {
                val toDo = Injection.provideToDoRepository(context).getToDoById(id)
                Injection.provideToDoRepository(context).updateToDo(toDo.copy(isCompleted = true))
                ToDoViewModel.instance?.apply {
                    notifiItemPos.postValue(
                        listTodo.value?.indexOf(
                            toDo
                        )
                    )
                }
            }
        } else if (ACTION_DELETE == action) {
            Log.i("ToDoChangeDoneReceiver", "onReceive: $action $id")
            CoroutineScope(Dispatchers.IO).launch {
                val toDo =
                    Injection.provideToDoRepository(context)
                        .getToDoById(id)
                Injection.provideToDoRepository(context).deleteToDo(
                    toDo
                )
            }
        }
        toDoDeleteNotification(context, id)
    }
}