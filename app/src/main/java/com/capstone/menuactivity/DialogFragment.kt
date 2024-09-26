package com.capstone.menuactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide


class DialogFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dialog, container, false)

        // Load the image with Glide
        val gifSample: ImageView = view.findViewById(R.id.image)
        Glide.with(this)
            .load(R.drawable.for_first)
            .into(gifSample)

        // Get references to the buttons
        val positiveBtn = view.findViewById<ImageButton>(R.id.positiveBtn)
        val negativeBtn = view.findViewById<ImageButton>(R.id.negativeBtn)

        // Set click listener for the positive button
        positiveBtn.setOnClickListener {
            // Navigate to First Fragment
            val fragment = FirstFragment() // Create an instance of FirstFragment
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment) // Replace current fragment with FirstFragment
                ?.addToBackStack(null) // Add to back stack if needed
                ?.commit()
        }

        // Set click listener for the negative button
        negativeBtn.setOnClickListener {
            // Exit the application
            activity?.finishAffinity() // Close the current activity
        }

        return view
    }

}