package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    val id: Long = System.currentTimeMillis(),
    val title: String,
    val description: String,
    val category: String,
    val scheduledDate: String)
    : Parcelable{
}