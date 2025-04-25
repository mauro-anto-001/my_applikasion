package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel() {
    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> = _task

    fun setTask(newTask: Task) {
        _task.value = newTask
    }

    fun clearTask() {
        _task.value = null
    }
}