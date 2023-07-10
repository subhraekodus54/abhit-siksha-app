package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.TopicItemLayoutBinding
import com.example.abithshiksha.model.pojo.get_topics.ResultX
import com.example.abithshiksha.view.activity.SubTopicActivity


class TopicListAdapter(private val itemList: MutableList<ResultX>,
                       private val context: Context):
    RecyclerView.Adapter<TopicListAdapter.TopicListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicListAdapter.TopicListViewHolder {
        val itemBinding = TopicItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopicListAdapter.TopicListViewHolder(itemBinding)
    }

    fun add(jobs: List<ResultX>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TopicListViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class TopicListViewHolder(private val itemBinding: TopicItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                topicTv.text = data?.name
                subtopicCountTv.text = data?.sub_topic_count.toString()
                rootLay.setOnClickListener {
                    val intent = Intent(context,SubTopicActivity::class.java)
                    intent.putExtra("name",data?.name)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
            }
        }
    }
}