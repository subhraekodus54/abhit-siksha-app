package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.SuggestionCourseItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.suggestion_courses.ResultX
import com.example.abithshiksha.view.activity.AuthActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class SuggestionCourseAdapter(private val itemList: List<ResultX>,
                              private val context: Context,
                              private val subjectClickListener: SubjectClickListener):
    RecyclerView.Adapter<SuggestionCourseAdapter.SuggestionCourseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionCourseViewHolder {
        val itemBinding = SuggestionCourseItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SuggestionCourseViewHolder(itemBinding,subjectClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SuggestionCourseViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class SuggestionCourseViewHolder(private val itemBinding: SuggestionCourseItemLayoutBinding, private val subjectClickListener: SubjectClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                priceTv.text = "₹"+data.subject_amount
                courseNameTv.text = data.subject_name+" || "+data.boards.exam_board+" || "+"Class "+data.assign_class.`class`
                if(data?.rating.toString() == "No reviews yet"){
                    ratingTv.gone()
                    starImg.gone()
                }else{
                    ratingTv.visible()
                    starImg.visible()
                    ratingTv.text = data?.rating.toString()
                }

                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(itemImg)

                rootLay.setOnClickListener {
                    subjectClickListener.onClick(it.rootView, data?.id)
                }

            }
        }
    }
}