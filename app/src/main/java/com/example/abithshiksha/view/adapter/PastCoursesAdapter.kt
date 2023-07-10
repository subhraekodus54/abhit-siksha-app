package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.PastCoursesItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.get_course_subject.CartSubjectDetail
import com.example.abithshiksha.view.activity.AddReviewActivity
import com.example.abithshiksha.view.activity.CourseDetailsActivity
import com.example.abithshiksha.view.activity.ProfileActivity

class PastCoursesAdapter(private val itemList: List<CartSubjectDetail>, private val context: Context):
    RecyclerView.Adapter<PastCoursesAdapter.PastCoursesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastCoursesAdapter.PastCoursesViewHolder {
        val itemBinding = PastCoursesItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PastCoursesAdapter.PastCoursesViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PastCoursesAdapter.PastCoursesViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class PastCoursesViewHolder(private val itemBinding: PastCoursesItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: CartSubjectDetail, context: Context) {

            itemBinding.apply {
                subjectNameTv.text = data?.name
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(itemImg)

                if(data?.total_lesson>1){
                    lessonCountTv.text = data?.total_lesson.toString()+" lessons"
                }else{
                    lessonCountTv.text = data?.total_lesson.toString()+" lesson"
                }

                //priceTv.text = data?.price.toString()

                reviewBtn.setOnClickListener {
                    val intent = Intent(context, AddReviewActivity::class.java)
                    intent.putExtra("id",data?.id)
                    intent.putExtra("image",data?.image)
                    intent.putExtra("name",data?.name)
                    context.startActivity(intent)
                }
                viewSubjectBtn.setOnClickListener {
                    val intent = Intent(context, CourseDetailsActivity::class.java)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
            }
        }

    }
}