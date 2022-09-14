package thuan.todolist.feature_todo.ui.add_edit_todo

sealed class AddEditToDoEvent {

    data class EnteredTitle(val title: String) : AddEditToDoEvent()

    data class EnteredDescription(val description: String) : AddEditToDoEvent()

    data class EnteredGroupName(val groupName: String) : AddEditToDoEvent()

    data class EnteredDateAndTime(val dateAndTime: String) : AddEditToDoEvent()

    class SaveToDo(val toastCallBack: (String) -> Unit,val pressBackCallBack: () -> Unit) : AddEditToDoEvent()
}
