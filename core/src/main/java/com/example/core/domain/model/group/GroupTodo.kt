package com.example.core.domain.model.group

data class GroupTodo(
    val name: String,
)

class InvalidGroupException(message: String) : Exception(message)
