package thuan.todolist.feature_todo.data.data_source

import androidx.lifecycle.LiveData
import androidx.room.*
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.GroupWithToDos


@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_table")
    fun getAllToDo(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table")
    suspend fun getAllToDoSync(): List<ToDo>

    @Query("SELECT * FROM group_todo_table WHERE group_name = :groupName")
    fun getAllToDoWithGroup(groupName: String): LiveData<GroupWithToDos>

    @Query("SELECT * FROM group_todo_table")
    fun getAllGroup(): LiveData<List<GroupToDo>>


    @Query("SELECT * FROM todo_table WHERE todo_is_completed = 0")
    fun getToDoNotDone(): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    fun getToDoById(id: Int): ToDo

    @Insert()
    suspend fun insertToDo(toDo: ToDo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupToDo)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateToDo(toDo: ToDo)

    @Delete
    suspend fun deleteToDoById(toDo: ToDo)

    @Transaction  //Transaction is used to make sure that all the queries are executed in a single transaction
    @Query("DELETE FROM todo_table")
    suspend fun deleteAllToDo()

    @Transaction
    @Query("DELETE FROM todo_table WHERE todo_is_completed = 1")
    suspend fun deleteAllCompletedToDo()
}