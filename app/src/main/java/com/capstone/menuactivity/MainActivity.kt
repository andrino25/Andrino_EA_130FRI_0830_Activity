package com.capstone.menuactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Replace ActionBar with custom Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        return true
    }

    // Handle menu item clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_first_fragment -> {
                // Navigate to FirstFragment using FragmentTransaction
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FirstFragment())
                    .addToBackStack(null) // Optional: allows user to press back to return to the previous fragment
                    .commit()
                return true
            }
            R.id.menu_second_fragment -> {
                // Navigate to SecondFragment using FragmentTransaction
                val dialog = DialogFragment() // Create instance of DialogFragment
                dialog.show(supportFragmentManager, "DialogFragment") // Show the DialogFragment
                return true
            }
            R.id.menu_exit -> {
                // Handle Exit App
                finishAffinity() // Closes the app
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}