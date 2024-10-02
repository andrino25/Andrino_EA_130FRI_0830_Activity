package com.capstone.bottomnavigation.ui.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.KeyEvent
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.capstone.bottomnavigation.R
import com.capstone.bottomnavigation.TaskAdapter
import com.capstone.bottomnavigation.databinding.FragmentDashboardBinding
import com.google.android.material.imageview.ShapeableImageView

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private val tasks = mutableListOf<String>()
    private lateinit var addBtn: ShapeableImageView
    private lateinit var adapter: TaskAdapter
    private lateinit var gestureDetector: GestureDetector

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ListView, EditText, and Add button from binding
        listView = binding.listView // Make sure this ID exists in your layout
        editText = binding.editText // Make sure this ID exists in your layout
        addBtn = binding.addBtn // Initialize the add button

        adapter = TaskAdapter(requireContext(), tasks)
        listView.adapter = adapter

        // Set up the gesture detector to detect double taps
        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val position = listView.pointToPosition(e.x.toInt(), e.y.toInt())
                if (position != AdapterView.INVALID_POSITION) {
                    showPopupMenu(listView.getChildAt(position), position)
                }
                return true
            }
        })

        // Add a touch listener to the ListView to capture touch events
        listView.setOnTouchListener { _, motionEvent ->
            gestureDetector.onTouchEvent(motionEvent)
            true
        }

        // Set up the add button click listener
        addBtn.setOnClickListener {
            val task = editText.text.toString()
            if (task.isNotBlank()) {
                tasks.add(task)
                adapter.notifyDataSetChanged()
                editText.text.clear() // Clear the EditText after adding the task
            }
        }

        return root
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit -> {
                    showEditTaskDialog(position)
                    true
                }
                R.id.delete -> {
                    tasks.removeAt(position)
                    adapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showEditTaskDialog(position: Int) {
        val currentTask = tasks[position]

        // Create an AlertDialog Builder
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Task")

        // Set up the input
        val input = EditText(requireContext())
        input.setText(currentTask)
        builder.setView(input)

        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->
            val updatedTask = input.text.toString()
            if (updatedTask.isNotBlank()) {
                tasks[position] = updatedTask // Update the task
                adapter.notifyDataSetChanged() // Notify adapter
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        // Show the dialog
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
