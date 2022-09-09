package thuan.todolist.feature_todo.data.repository

import androidx.lifecycle.LiveData
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos
import thuan.todolist.feature_todo.domain.model.ToDo

interface ToDoRepository {
    fun getAllToDo(): LiveData<List<ToDo>>
    fun getAllToDoWithGroup(groupName : String): LiveData<List<GroupWithToDos>>
    suspend fun deleteToDo(toDo: ToDo)
    suspend fun insertToDo(toDo: ToDo)
    suspend fun insertGroup(group: GroupToDo)
    suspend fun updateToDo(toDo: ToDo)
    suspend fun deleteAllToDo()
    suspend fun deleteAllCompletedToDo()
}