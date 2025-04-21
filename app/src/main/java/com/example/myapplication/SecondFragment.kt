package com.example.myapplication

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding
import java.util.Calendar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dateButton: Button
    private var selectedDate: String = getTodaysDate()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dateButton = binding.datePickerButton
        selectedDate = getTodaysDate()
        dateButton.text = selectedDate
        initDatePicker()
        //for the add button
        binding.buttonFirst.setOnClickListener() {
            val title = binding.title.text.toString()
            val description = binding.description.text.toString()
            val category = binding.category.text.toString()
            //
            if(title.isNotBlank() && description.isNotBlank() && category.isNotBlank() && selectedDate.isNotBlank()) {
                val newTask = Task(title,description, category, selectedDate)
                findNavController().previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("new_task", newTask)
                findNavController().popBackStack()
            }
        }
        dateButton.setOnClickListener{
            openDatePicker(it)
        }
    }

    private fun getTodaysDate(): String {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }
    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener{ _: DatePicker, year:
        Int, month: Int, day: Int ->
            val adjustedMonth = month + 1
            val date = makeDateString(day, adjustedMonth, year)
            selectedDate = date //update default date to selected date
            dateButton.text = date
        }
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        datePickerDialog = DatePickerDialog(requireContext(), dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "${getMonthFormat(month)} $day $year"
    }

    private fun getMonthFormat(month: Int): String {
        return when (month) {
            1 -> "JAN"
            2 -> "FEB"
            3 -> "MAR"
            4 -> "APR"
            5 -> "MAY"
            6 -> "JUN"
            7 -> "JULY"
            8 -> "AUG"
            9 -> "SEP"
            10 -> "OCT"
            11 -> "NOV"
            12 -> "DEC"
            else -> "JAN" // default if nothing is selected which shouldnt happen
        }
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}