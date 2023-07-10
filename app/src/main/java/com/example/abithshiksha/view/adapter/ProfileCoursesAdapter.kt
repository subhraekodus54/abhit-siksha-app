package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.ProfileCoursesItemLayoutBinding
import com.example.abithshiksha.model.pojo.get_orders.Course
import com.example.abithshiksha.view.activity.MySubjectsActivity
import com.thekhaeng.pushdownanim.PushDownAnim

class ProfileCoursesAdapter (private val itemList: List<Course>, private val context: Context):
    RecyclerView.Adapter<ProfileCoursesAdapter.ProfileCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileCoursesAdapter.ProfileCoursesViewHolder {
        val itemBinding = ProfileCoursesItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileCoursesAdapter.ProfileCoursesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: ProfileCoursesAdapter.ProfileCoursesViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }
    class ProfileCoursesViewHolder(private val itemBinding: ProfileCoursesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Course, context: Context) {

            itemBinding.apply {
                courseNameTv.text = data?.board+" || "+"Class "+data?.class_name
                if(data?.total_subject>1){
                    subjectsCountTv.text = data?.total_subject.toString()+" Subjects"
                }else{
                    subjectsCountTv.text = data?.total_subject.toString()+" Subject"
                }

                PushDownAnim.setPushDownAnimTo(rootLay).setOnClickListener {
                    val intent = Intent(context, MySubjectsActivity::class.java)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}