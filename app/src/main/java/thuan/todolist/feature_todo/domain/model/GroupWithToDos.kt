package thuan.todolist.feature_todo.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class GroupWithToDos(
    @Embedded val group: GroupToDo,  // Embedded annotation is used to tell Room that this is a nested object
    @Relation(                      // Relation annotation is used to tell Room that this is a relation
        parentColumn = "group_name",
        entityColumn = "group_name"
    )
    val todos: List<ToDo>
)
