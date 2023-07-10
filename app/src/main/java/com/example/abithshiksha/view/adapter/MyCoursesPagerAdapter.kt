package com.example.abithshiksha.view.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.abithshiksha.view.fragment.MyCoursesFragment
import com.example.abithshiksha.view.fragment.OngoingCoursesFragment
import com.example.abithshiksha.view.fragment.MySubjectsFragment

class MyCoursesPagerAdapter(fragment: MyCoursesFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngoingCoursesFragment()
            else -> MySubjectsFragment()
        }
    }
}