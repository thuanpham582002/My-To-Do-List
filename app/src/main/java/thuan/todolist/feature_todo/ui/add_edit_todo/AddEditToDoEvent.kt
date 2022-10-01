package thuan.todolist.feature_todo.ui.add_edit_todo

import java.util.*

sealed interface AddEditToDoEvent {

    data class EnteredTitle(val title: String) : AddEditToDoEvent

    data class EnteredDescription(val description: String) : AddEditToDoEvent

    data class EnteredGroupName(val groupName: String) : AddEditToDoEvent

    data class EnteredDateAndTime(val dateAndTime: Date?) : AddEditToDoEvent

    class SaveGroup(val groupName: String) : AddEditToDoEvent
    data class EnteredIsDone(val isCompleted: Boolean) : AddEditToDoEvent
    object SaveToDo : AddEditToDoEvent
    object DeleteToDo : AddEditToDoEvent
}
