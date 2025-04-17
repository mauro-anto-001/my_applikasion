package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val taskList: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    //viewholder to hold a row of the list aka a card/task
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.task_description)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
         return TaskViewHolder(view)
     }

     override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
         val task = taskList[position]
         holder.titleTextView.text =task.title
         holder.descriptionTextView.text = task.description
     }

     override fun getItemCount(): Int = taskList.size
}