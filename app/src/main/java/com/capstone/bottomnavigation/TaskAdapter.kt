package com.capstone.bottomnavigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class TaskAdapter(context: Context, private val tasks: List<String>) :
    ArrayAdapter<String>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)

        val taskTextView: TextView = view.findViewById(R.id.listText)
        val taskImageView: ImageView = view.findViewById(R.id.image)

        // Set the task text
        taskTextView.text = getItem(position)

        // Set the default image
        taskImageView.setImageResource(R.drawable.ic_launcher_foreground)

        return view
    }
}