package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.LessonsItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.helper.click_listener.TopicClickListener
import com.example.abithshiksha.model.pojo.get_lessons.Data
import com.example.abithshiksha.model.pojo.get_topics.ResultX
import com.example.abithshiksha.view.activity.ChapterContentActivity
import com.example.abithshiksha.view.activity.LessonsActivity
import com.example.abithshiksha.view.activity.SubTopicActivity
import com.thekhaeng.pushdownanim.PushDownAnim

class LessonsListAdapter(private val itemList: MutableList<Data>,
                         private val context: Context,
                         private val topicClickListener: TopicClickListener
):
    RecyclerView.Adapter<LessonsListAdapter.LessonsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonsListAdapter.LessonsListViewHolder {
        val itemBinding = LessonsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonsListAdapter.LessonsListViewHolder(itemBinding, topicClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<Data>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: LessonsListAdapter.LessonsListViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class LessonsListViewHolder(private val itemBinding: LessonsItemLayoutBinding, private val topicClickListener: TopicClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Data, context: Context) {

            itemBinding.apply {
                val totalCount = data?.total_content.total_video+data?.total_content.total_pdf+data?.total_content.total_article
                chapterNameTv.text = data?.name
                if(totalCount>1){
                    videoCountTv.text = totalCount.toString() + " Topics"
                }else{
                    videoCountTv.text = totalCount.toString() + " Topic"
                }
                playBtn.setOnClickListener {
                    val intent = Intent(context, SubTopicActivity::class.java)
                    intent.putExtra("name",data?.name)
                    intent.putExtra("id",data?.id)
                    intent.putExtra("mcq",data?.total_content.total_mcq_set)
                    context.startActivity(intent)
                }

                PushDownAnim.setPushDownAnimTo(topicBtn).setOnClickListener {
                    topicClickListener.onTopicClick(it.rootView, data?.id)
                }
            }
        }

    }
}