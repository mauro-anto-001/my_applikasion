package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFirstBinding


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), TaskAdapter.OnTaskClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private var taskList: MutableList<Task> = mutableListOf()

    private var _binding: FragmentFirstBinding? = null
    private val taskViewModel: TaskViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        taskAdapter = TaskAdapter(taskList,this)
        recyclerView.adapter = taskAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navBackStackEntry = findNavController().currentBackStackEntry
        val savedStateHandle = navBackStackEntry?.savedStateHandle

        savedStateHandle?.getLiveData<Task>("new_task")?.observe(viewLifecycleOwner){ newTask ->
            taskList.add(newTask)
            taskAdapter.notifyItemInserted(taskList.size - 1)
        }

        savedStateHandle?.getLiveData<Task>("edited_task")?.observe(viewLifecycleOwner) { editedTask ->
            val index = taskList.indexOfFirst { it.id == editedTask.id }
            if (index != -1) {
                taskList[index] = editedTask
                taskAdapter.notifyItemChanged(index)
            }
            else {
                Log.e("FirstFragment", "Task not found for editing")  // If the task is not found
            }
        }
    }
    override fun onEditClick(task:Task){

        taskViewModel.setTask(task)
        val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(task)
        findNavController().navigate(action)
        }
    override fun onDeleteClick(task: Task){
        //remove task from list
        val index = taskList.indexOf(task)
        if(index != -1) {
            taskList.removeAt(index)
            taskAdapter.notifyItemRemoved(index)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}