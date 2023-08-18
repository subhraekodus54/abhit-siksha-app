package com.example.abithshiksha.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ActivityLessonsBinding
import com.example.abithshiksha.databinding.ActivitySubTopicBinding
import com.example.abithshiksha.view.adapter.SubTopicPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.user.caregiver.isConnectedToInternet
import com.user.caregiver.lightStatusBar

class SubTopicActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubTopicBinding
    private var topic_name: String = "No data found"
    private var topic_id: Int = 0
    private var mcq_count: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubTopicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightStatusBar(this,R.color.white)

        val extras = intent.extras
        if (extras != null) {
            topic_name = intent?.getStringExtra("name")!!
            topic_id = intent?.getIntExtra("id",0)!!
            mcq_count = intent?.getIntExtra("mcq",0)!!
        }

        mcq_count?.let {
            if(mcq_count>1){
                binding.mcqTestHtv.text = mcq_count.toString()+" MCQ Tests"
            }else{
                if(mcq_count>0){
                    binding.mcqTestHtv.text = mcq_count.toString()+" MCQ Test"
                }else{
                    binding.mcqTestHtv.text = "MCQ Test"
                }
            }
        }

        binding.topicHtv.text = topic_name
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.playTestLay.setOnClickListener {
            val intent = Intent(this, McqSetsActivity::class.java)
            intent.putExtra("id",topic_id)
            intent.putExtra("lesson_name",topic_name)
            startActivity(intent)
        }


        if(isConnectedToInternet()){
            setUpTabLayoutWithViewPager()
        }else{
            Toast.makeText(this,"No internet connection.", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpTabLayoutWithViewPager() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Videos"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Docs"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Articles"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Addon"))

        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = SubTopicPagerAdapter(this, this.supportFragmentManager, binding.tabLayout.tabCount, topic_id)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

}