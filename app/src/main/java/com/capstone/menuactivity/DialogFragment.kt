package com.capstone.menuactivity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide

// Make sure this class extends DialogFragment
class DialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use AlertDialog.Builder to build the dialog
        val builder = AlertDialog.Builder(requireContext())

        // Inflate your custom layout for the dialog
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_dialog, null)

        // Load the image with Glide
        val gifSample: ImageView = view.findViewById(R.id.image)
        Glide.with(this)
            .load(R.drawable.for_first) // Load your drawable
            .into(gifSample)


        builder.setView(view)
            .setTitle("Menu Activity") // Set dialog title
            .setMessage("This is using dialog in a dialog fragment.") // Set dialog message
            .setPositiveButton("Positive") { dialog, _ ->
                // Perform navigation to FirstFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FirstFragment())
                    .addToBackStack(null)
                    .commit()

                dialog.dismiss() // Close the dialog after navigation
            }

            .setNegativeButton("Negative") { dialog, _ ->
                dialog.dismiss()
                requireActivity().finish()
            }


        return builder.create()
    }
}
