package com.example.myapplication

import java.io.Serializable
import java.util.Date

data class Task(
    val title: String,
    val description: String,
    val category: String,
    val scheduledDate: String)
    : Serializable{
}