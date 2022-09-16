package thuan.todolist.feature_todo.domain.use_case

data class ToDoUseCases(
    val getToDos: GetToDos,
    val addToDo: AddToDo,
    val addGroup: AddGroup,
    val updateToDo: UpdateToDo,
    val deleteToDo: DeleteToDo
)
