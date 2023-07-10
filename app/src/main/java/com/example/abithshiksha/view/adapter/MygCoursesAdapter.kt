package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.MyCoursesItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_orders.Course
import com.example.abithshiksha.view.activity.AuthActivity
import com.example.abithshiksha.view.activity.CartDetailsActivity
import com.example.abithshiksha.view.activity.MySubjectsActivity
import com.thekhaeng.pushdownanim.PushDownAnim

class MygCoursesAdapter (private val itemList: List<Course>, private val context: Context):
    RecyclerView.Adapter<MygCoursesAdapter.OngoingCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MygCoursesAdapter.OngoingCoursesViewHolder {
        val itemBinding = MyCoursesItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MygCoursesAdapter.OngoingCoursesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MygCoursesAdapter.OngoingCoursesViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class OngoingCoursesViewHolder(private val itemBinding: MyCoursesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Course, context: Context) {
            itemBinding.apply {
                var subjects: String = ""
                boardNameTv.text = data?.board
                classNameTv.text = "Class "+data?.class_name

                if(data?.total_subject>1){
                    subjectCountTxt.text = data?.total_subject.toString()+" Subjects"
                }else{
                    subjectCountTxt.text = data?.total_subject.toString()+" Subject"
                }

                priceTv.text = "₹"+data?.total_amount.toString()
                for (i in data?.cart_subject_details){
                    if(subjects.isEmpty()){
                        subjects += i
                    }else{
                        subjects += ","+i
                    }
                }

                if(subjects.length<50){
                    subjectsNameTv.text = subjects
                }else{
                    subjectsNameTv.text = subjects.substring(0,50)+"...";
                }

                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.board_logo) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(boardImgView)

                PushDownAnim.setPushDownAnimTo(rootLay).setOnClickListener {
                    val intent = Intent(context, MySubjectsActivity::class.java)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
            }
        }

    }
}