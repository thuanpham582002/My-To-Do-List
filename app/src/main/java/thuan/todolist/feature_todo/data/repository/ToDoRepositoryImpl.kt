package thuan.todolist.feature_todo.data.repository

import androidx.lifecycle.LiveData
import thuan.todolist.feature_todo.data.data_source.ToDoDao
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos
import thuan.todolist.feature_todo.domain.model.ToDo

class ToDoRepositoryImpl(private val dao: ToDoDao) : ToDoRepository {
    override fun getAllToDo(): LiveData<List<ToDo>> {
        return dao.getAllToDo()
    }

    override fun getToDoById(id: Int): ToDo {
        return dao.getToDoById(id)
    }

    override suspend fun getAllToDoSync(): List<ToDo> {
        return dao.getAllToDoSync()
    }

    override fun getAllToDoWithGroup(groupName: String): LiveData<GroupWithToDos> {
        return dao.getAllToDoWithGroup(groupName)
    }

    override fun getAllGroup(): LiveData<List<GroupToDo>> {
        return dao.getAllGroup()
    }

    override suspend fun deleteToDo(toDo: ToDo) {
        dao.deleteToDoById(toDo)
    }

    override suspend fun insertToDo(toDo: ToDo): Long {
        return dao.insertToDo(toDo)
    }

    override suspend fun insertGroup(group: GroupToDo) {
        dao.insertGroup(group)
    }

    override suspend fun updateToDo(toDo: ToDo) {
        dao.updateToDo(toDo)
    }

    override suspend fun deleteAllToDo() {
        dao.deleteAllToDo()
    }

    override suspend fun deleteAllCompletedToDo() {
        dao.deleteAllCompletedToDo()
    }
}