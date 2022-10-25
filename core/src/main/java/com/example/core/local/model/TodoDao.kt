package com.example.core.data.source.local.model

import androidx.room.*
import com.example.core.data.source.local.model.group.GroupTodoEntity
import com.example.core.data.source.local.model.relation.GroupWithTodos
import com.example.core.data.source.local.model.todo.TodoEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table")
    fun getAllTodo(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM group_todo_table WHERE group_name = :groupName")
    fun getAllTodoWithGroup(groupName: String): Flow<GroupWithTodos>

    @Query("SELECT * FROM group_todo_table")
    fun getAllGroup(): Flow<List<GroupTodoEntity>>


    @Query("SELECT * FROM todo_table WHERE todo_is_completed = 0")
    fun getTodoNotDone(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todo_table WHERE id = :id")
    fun getTodoById(id: Long): TodoEntity?

    @Query("SELECT * FROM group_todo_table WHERE group_id = :id")
    fun getGroupById(id: Long): GroupTodoEntity?

    @Insert
    suspend fun insertTodo(toDo: TodoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupTodoEntity)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(toDo: TodoEntity)

    @Delete
    suspend fun deleteTodoById(toDo: TodoEntity)

    @Transaction  //Transaction is used to make sure that all the queries are executed in a single transaction
    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodo()

    @Transaction
    @Query("DELETE FROM todo_table WHERE todo_is_completed = 1")
    suspend fun deleteAllCompletedTodo()

    @Delete
    @Transaction
    suspend fun deleteGroup(groupTodoEntity: GroupTodoEntity)
}