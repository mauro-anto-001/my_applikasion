package com.example.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = intent.getIntExtra("userId", -1)

        val toolbar = findViewById<Toolbar>(R.id.topToolbar)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = findViewById(R.id.taskRecyclerView)
        taskAdapter = TaskAdapter(taskList,
            onEdit = { task -> showTaskDialog(task) },
            onDelete = { task ->
                thread {
                    AppDatabase.getInstance(this).taskDao().delete(task)
                }
                taskList.remove(task)
                taskAdapter.notifyDataSetChanged()
                updateTaskCount()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        findViewById<FloatingActionButton>(R.id.addTaskFab).setOnClickListener {
            showTaskDialog()
        }
        loadTaskFromDataBase()
        updateTaskCount()
        applyUserSettings()
    }

    private fun loadTaskFromDataBase(){
        thread {
            val tasks = AppDatabase.getInstance(this).taskDao().getTasksByUserId(userId)
            runOnUiThread{
                taskList.clear()
                taskList.addAll(tasks)
                taskAdapter.notifyDataSetChanged()
                updateTaskCount()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        applyUserSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun applyUserSettings() {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)

        val font = when (prefs.getString("fontType", "default")) {
            "mono" -> Typeface.MONOSPACE
            "serif" -> Typeface.SERIF
            else -> Typeface.DEFAULT
        }

        val fontSize = when (prefs.getString("fontSize", "medium")) {
            "small" -> 14f
            "large" -> 20f
            else -> 16f
        }

        val isDark = prefs.getBoolean("darkMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        findViewById<TextView>(R.id.taskCountText).apply {
            typeface = font
            textSize = fontSize
        }

        // Apply to all visible task items
        val recyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        for (i in 0 until taskAdapter.itemCount) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? TaskAdapter.TaskViewHolder ?: continue
            holder.title.typeface = font
            holder.desc.typeface = font
            holder.cat.typeface = font
            holder.due.typeface = font

            holder.title.textSize = fontSize
            holder.desc.textSize = fontSize
            holder.cat.textSize = fontSize
            holder.due.textSize = fontSize
        }
    }

    fun updateTaskCount() {
        val countText = findViewById<TextView>(R.id.taskCountText)
        val icon = findViewById<ImageView>(R.id.emptyIcon)

        val remainingTasks = taskList.count { !it.isCompleted }

        if (remainingTasks == 0) {
            countText.text = "No tasks left"
            icon?.visibility = ImageView.VISIBLE
        } else {
            countText.text = "$remainingTasks task(s) to do"
            icon?.visibility = ImageView.GONE
        }

        countText.animate()
            .alpha(0f)
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(150)
            .withEndAction {
                countText.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .start()
            }.start()
    }


    private fun showTaskDialog(task: Task? = null) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_task, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.titleInput)
        val descInput = dialogView.findViewById<EditText>(R.id.descriptionInput)
        val catInput = dialogView.findViewById<EditText>(R.id.categoryInput)
        val dueInput = dialogView.findViewById<EditText>(R.id.dueDateTimeInput)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        var selectedDateTime = ""

        dueInput.isFocusable = false
        dueInput.isClickable = true
        dueInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                TimePickerDialog(this, { _, hour, minute ->
                    val selectedCal = Calendar.getInstance()
                    selectedCal.set(year, month, day, hour, minute)
                    selectedDateTime = dateFormat.format(selectedCal.time)
                    dueInput.setText(selectedDateTime)
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        if (task != null) {
            titleInput.setText(task.title)
            descInput.setText(task.description)
            catInput.setText(task.category)
            dueInput.setText(task.dueDateTime)
            selectedDateTime = task.dueDateTime
        }

        AlertDialog.Builder(this)
            .setTitle(if (task == null) "Add Task" else "Edit Task")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val newTask = Task(
                    task?.id ?: 0,
                    titleInput.text.toString().trim(),
                    descInput.text.toString().trim(),
                    catInput.text.toString().trim(),
                    selectedDateTime,
                    isCompleted = task?.isCompleted ?: false,
                    userId = userId
                )
                thread {
                    val dao = AppDatabase.getInstance(this).taskDao()
                    if (task == null) {
                        val id = dao.insert(newTask).toInt()
                        newTask.id = id
                        runOnUiThread{
                            taskList.add(newTask)
                            taskAdapter.notifyDataSetChanged()
                            updateTaskCount()
                        }
                    }
                    else {
                        dao.update(newTask)
                        runOnUiThread{
                            val index = taskList.indexOfFirst { it.id == task.id }
                            if (index != -1) taskList[index] = newTask
                            taskAdapter.notifyDataSetChanged()
                            updateTaskCount()
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
