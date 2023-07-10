package com.example.abithshiksha.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ReviewsItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_review.Review

class ReviewsAdapter(private val itemList: List<Review>, private val context: Context):
    RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsAdapter.ReviewsViewHolder {
        val itemBinding = ReviewsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewsAdapter.ReviewsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: ReviewsAdapter.ReviewsViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }
    class ReviewsViewHolder(private val itemBinding: ReviewsItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Review, context: Context) {

            itemBinding.apply {
                reviewTv.text = data?.review
                ratingTv.text = data?.rating.toString()
                nameTv.text = data?.user_name
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(imageView)
            }
        }

    }
}