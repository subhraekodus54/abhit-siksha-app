package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.VideosListItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.helper.click_listener.VideoClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_lessons.Data
import com.example.abithshiksha.model.pojo.get_videos.Video
import com.example.abithshiksha.view.activity.ChapterContentActivity
import com.example.abithshiksha.view.activity.VideoPlayActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class VideoListAdapter(private val itemList: MutableList<Video>,
                       private val context: Context,
                       private val videoClickListener: VideoClickListener
):
    RecyclerView.Adapter<VideoListAdapter.VideoListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListAdapter.VideoListViewHolder {
        val itemBinding = VideosListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoListAdapter.VideoListViewHolder(itemBinding, videoClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<Video>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class VideoListViewHolder(private val itemBinding: VideosListItemLayoutBinding, private val videoClickListener: VideoClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Video, context: Context) {

            itemBinding.apply {
                nameTv.text = data?.title
                videoDurationTv.text = data?.video_duration
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.video_thumbnail_image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(thumbnailImg)

                if(data?.purchase == 1){
                    lockBtn.gone()
                    playBtn.visible()
                    rootLay.setOnClickListener {
                        videoClickListener.onClick(it.rootView, data?.id, data?.original_video_path)
                    }
                }else{
                    lockBtn.visible()
                    playBtn.gone()
                    rootLay.setOnClickListener {
                        Toast.makeText(context,"You have to purchase the subject to view this.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}