package com.example.abithshiksha.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityNotificationBinding
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.view.adapter.NotificationAdapter
import com.user.caregiver.lightStatusBar

class NotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val suggestionCourseList = ArrayList<TestModel>()
        suggestionCourseList.add(TestModel("https://media.istockphoto.com/photos/teenager-girl-during-homeschooling-picture-id1307257555"))
        suggestionCourseList.add(TestModel("https://images.unsplash.com/photo-1623076189461-f7706b741c04?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTd8fG9ubGluZSUyMGxlYXJuaW5nfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=500&q=60"))
        suggestionCourseList.add(TestModel("https://images.unsplash.com/photo-1560785496-3c9d27877182?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=774&q=80"))
        suggestionCourseList.add(TestModel("https://images.unsplash.com/photo-1597933471507-1ca5765185d8?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8b25saW5lJTIwbGVhcm5pbmd8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"))
        fillNotificationRecycler(suggestionCourseList)

        binding.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun fillNotificationRecycler(list: List<TestModel>) {
        val gridLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.notificationRecycler.apply {
            layoutManager = gridLayoutManager
            adapter = NotificationAdapter(list,this@NotificationActivity)
        }
    }
}