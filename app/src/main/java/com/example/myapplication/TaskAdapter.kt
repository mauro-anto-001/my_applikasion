package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskAdapter(
    private val taskList: List<Task>, private val listener: OnTaskClickListener)
    : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    //viewholder to hold a row of the list aka a card/task
    interface OnTaskClickListener {
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
    }
    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView = itemView.findViewById(R.id.task_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.task_description)
        val categoryTextView: TextView = itemView.findViewById(R.id.task_category)
        val scheduledDateTextView: TextView = itemView.findViewById(R.id.task_schedule)
        val fabEdit: FloatingActionButton = itemView.findViewById(R.id.fab_edit)
        val fabDelete: FloatingActionButton = itemView.findViewById(R.id.fab_delete)
    }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
         val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
         return TaskViewHolder(view)
     }

     override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
         val task = taskList[position]
         holder.titleTextView.text =task.title
         holder.descriptionTextView.text = task.description
         holder.categoryTextView.text = task.category
         holder.scheduledDateTextView.text = task.scheduledDate

         holder.fabEdit.setOnClickListener{
             listener.onEditClick(task)

         }
         holder.fabDelete.setOnClickListener {
             listener.onDeleteClick(task)
         }
     }

     override fun getItemCount(): Int = taskList.size

}