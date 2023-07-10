package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.UpcomingCourseItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.upcomming_response.ResultX
import com.example.abithshiksha.view.activity.CourseDetailsActivity

class UpcomingCourseAdapter (private val itemList: List<ResultX>,
                             private val context: Context,
                             private val subjectClickListener: SubjectClickListener):
    RecyclerView.Adapter<UpcomingCourseAdapter.UpcomingCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingCourseAdapter.UpcomingCourseViewHolder {
        val itemBinding = UpcomingCourseItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UpcomingCourseAdapter.UpcomingCourseViewHolder(itemBinding, subjectClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: UpcomingCourseAdapter.UpcomingCourseViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class UpcomingCourseViewHolder(private val itemBinding: UpcomingCourseItemLayoutBinding, private val subjectClickListener: SubjectClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                titleTv.text = data.subject_name+" || "+data.boards.exam_board+" || "+"Class "+data.assign_class.`class`
                priceTv.text = "₹"+data.subject_amount.toString()
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(itemImg)

                rootLay.setOnClickListener {
                    val intent = Intent(context, CourseDetailsActivity::class.java)
                    intent.putExtra("id",data?.id)
                    intent.putExtra("show_enroll",false)
                    context.startActivity(intent)
                }
            }
        }

    }
}