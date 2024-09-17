package com.capstone.newsactivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HeadlineFragment : Fragment(R.layout.fragment_headline_list) {

    private lateinit var sampleHeadlines: List<String>
    private lateinit var sampleContents: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the strings from resources
        sampleHeadlines = listOf(
            getString(R.string.headline_1),
            getString(R.string.headline_2),
            getString(R.string.headline_3)
        )

        sampleContents = listOf(
            getString(R.string.content_1),
            getString(R.string.content_2),
            getString(R.string.content_3)
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyItemRecyclerViewAdapter(sampleHeadlines) { position ->
            val content = sampleContents[position]
            (activity as? MainActivity)?.showContentFragment(content)
        }
    }
}

