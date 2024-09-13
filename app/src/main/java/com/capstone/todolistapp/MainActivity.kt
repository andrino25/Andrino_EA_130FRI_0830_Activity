package com.capstone.todolistapp

import android.os.Bundle
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var editText: EditText
    private val tasks = mutableListOf<String>()
    private lateinit var adapter: TaskAdapter
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        editText = findViewById(R.id.editText)

        adapter = TaskAdapter(this, tasks)
        listView.adapter = adapter

        // Set up the gesture detector to detect double taps
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
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

        // Add text directly to the list when "Enter" is pressed in the EditText
        editText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val task = editText.text.toString()
                if (task.isNotBlank()) {
                    tasks.add(task)
                    adapter.notifyDataSetChanged()
                    editText.text.clear() // Clear the EditText after adding the task
                }
                true
            } else {
                false
            }
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
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
        val task = tasks[position]
        editText.setText(task)
        tasks.removeAt(position)
        adapter.notifyDataSetChanged()
    }
}
