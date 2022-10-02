package thuan.todolist.feature_todo.ui.add_edit_todo

sealed interface AddAndEditState{
    object ToDoSaved: AddAndEditState
    object ToDoUpdated: AddAndEditState
    object ToDoDeleted: AddAndEditState
    object ToDoNotSaved: AddAndEditState
    object ToDoNotUpdated: AddAndEditState
    object ToDoNotDeleted: AddAndEditState
    object NoAction: AddAndEditState
    object GroupSaved : AddAndEditState
    object GroupNotSaved : AddAndEditState
}