package com.example.myapplication

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.concurrent.thread

class TaskAdapter(
    private val tasks: List<Task>,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.taskTitle)
        val desc = view.findViewById<TextView>(R.id.taskDescription)
        val cat = view.findViewById<TextView>(R.id.taskCategory)
        val due = view.findViewById<TextView>(R.id.taskDueDateTime)
        val editBtn = view.findViewById<Button>(R.id.editBtn)
        val deleteBtn = view.findViewById<Button>(R.id.deleteBtn)
        val checkBox = view.findViewById<CheckBox>(R.id.taskCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.title.text = task.title
        holder.desc.text = task.description
        holder.cat.text = "Category: ${task.category}"
        holder.due.text = "Due: ${task.dueDateTime}"

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = task.isCompleted

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            task.isCompleted = isChecked
            thread{
                AppDatabase.getInstance(holder.itemView.context).taskDao().update(task)
            }
            notifyItemChanged(position)
            (holder.itemView.context as? MainActivity)?.updateTaskCount()
        }

        val flag = if (task.isCompleted) Paint.STRIKE_THRU_TEXT_FLAG else 0
        holder.title.paintFlags = flag
        holder.desc.paintFlags = flag
        holder.cat.paintFlags = flag
        holder.due.paintFlags = flag

        holder.editBtn.setOnClickListener { onEdit(task) }
        holder.deleteBtn.setOnClickListener { onDelete(task) }
    }

    override fun getItemCount() = tasks.size
}
