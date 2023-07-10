package com.example.abithshiksha.view.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.abithshiksha.view.fragment.ArticleListFragment
import com.example.abithshiksha.view.fragment.PdfListFragment
import com.example.abithshiksha.view.fragment.VideoListFragment

class SubTopicPagerAdapter(var context: Context,
                           fm: FragmentManager,
                           var totalTabs: Int,
                           var topic_id: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                VideoListFragment(topic_id)
            }
            1 -> {
                PdfListFragment(topic_id)
            }
            2 -> {
                ArticleListFragment(topic_id)
            }

            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }

}