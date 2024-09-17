package com.capstone.newsactivity

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            // Show ListFragment initially
            val listFragment = HeadlineFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, listFragment)
                .commit()
        }
    }

    fun showContentFragment(content: String) {
        val contentFragment = ContentFragment.newInstance(content)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById<View>(R.id.fragment_content_container).visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_content_container, contentFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, contentFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        handleOrientationChange()
    }

    private fun handleOrientationChange() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            findViewById<View>(R.id.fragment_content_container).visibility = View.VISIBLE
        } else {
            findViewById<View>(R.id.fragment_content_container).visibility = View.GONE
        }
    }
}
