package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Task(
    var id: Int,
    var title: String,
    var description: String,
    var category: String,
    var dueDateTime: String,
    var isCompleted: Boolean = false
)
