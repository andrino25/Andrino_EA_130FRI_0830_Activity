package com.capstone.bottomnavigation.ui.notifications

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.capstone.bottomnavigation.R
import com.capstone.bottomnavigation.databinding.FragmentNotificationsBinding
import com.squareup.picasso.Picasso
import java.io.IOException

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Load saved preferences
        loadSavedData()

        // Profile image click listener
        binding.profile.setOnClickListener {
            openGallery()
        }

        // Save button click listener
        binding.saveBtn.setOnClickListener {
            saveDataToSharedPrefs()
            Toast.makeText(requireContext(), "Profile Saved!", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Open gallery to pick an image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handle image result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri? = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedImageUri)
                binding.profile.setImageBitmap(bitmap)

                // Save the selected image URI in SharedPreferences
                sharedPreferences.edit().putString("photoUri", selectedImageUri.toString()).apply()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    // Save data to SharedPreferences
    private fun saveDataToSharedPrefs() {
        val editor = sharedPreferences.edit()

        // Save EditText values
        editor.putString("name", binding.name.text.toString())
        editor.putString("address", binding.address.text.toString())
        editor.putString("contactNum", binding.contactNum.text.toString())

        // Save selected gender (RadioButton)
        val selectedGenderId = binding.radioGroup.checkedRadioButtonId
        val selectedGender = view?.findViewById<RadioButton>(selectedGenderId)?.text.toString()
        editor.putString("gender", selectedGender)

        // Save selected hobbies (CheckBoxes)
        editor.putBoolean("sports", binding.sports.isChecked)
        editor.putBoolean("reading", binding.reading.isChecked)
        editor.putBoolean("movies", binding.movies.isChecked)
        editor.putBoolean("sleeping", binding.sleeping.isChecked)
        editor.putBoolean("coding", binding.coding.isChecked)
        editor.putBoolean("drawing", binding.drawing.isChecked)

        editor.apply()
    }

    // Load saved data from SharedPreferences
    private fun loadSavedData() {
        // Load EditText values
        binding.name.setText(sharedPreferences.getString("name", ""))
        binding.address.setText(sharedPreferences.getString("address", ""))
        binding.contactNum.setText(sharedPreferences.getString("contactNum", ""))

        // Load selected gender (RadioButton)
        val savedGender = sharedPreferences.getString("gender", "")
        if (savedGender != null) {
            when (savedGender) {
                getString(R.string.radioButton1) -> binding.radioButton1.isChecked = true
                getString(R.string.radioButton2) -> binding.radioButton2.isChecked = true
                getString(R.string.radioButton3) -> binding.radioButton3.isChecked = true
            }
        }

        // Load CheckBox values
        binding.sports.isChecked = sharedPreferences.getBoolean("sports", false)
        binding.reading.isChecked = sharedPreferences.getBoolean("reading", false)
        binding.movies.isChecked = sharedPreferences.getBoolean("movies", false)
        binding.sleeping.isChecked = sharedPreferences.getBoolean("sleeping", false)
        binding.coding.isChecked = sharedPreferences.getBoolean("coding", false)
        binding.drawing.isChecked = sharedPreferences.getBoolean("drawing", false)

        // Load saved photo URI
        val savedPhotoUri = sharedPreferences.getString("photoUri", null)
        if (savedPhotoUri != null) {
            // Use Picasso to load the image and show an error placeholder if it fails
            Picasso.get()
                .load(savedPhotoUri)
                .error(R.drawable.ic_profile_icon) // Replace with your error placeholder drawable
                .into(binding.profile)
        }
    }
}