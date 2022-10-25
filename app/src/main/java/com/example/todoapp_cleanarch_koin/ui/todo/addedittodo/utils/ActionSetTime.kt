package com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.utils

import java.io.Serializable
import java.util.*

interface ActionSetTime : Serializable {
    fun setTime(dateAndTime: Date?)
}