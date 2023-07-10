package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.TopicListItemLayoutBinding
import com.example.abithshiksha.model.pojo.get_topic_list.ResultX

class TopicListAdapter2(private val itemList: List<ResultX>,
                        private val context: Context
):
    RecyclerView.Adapter<TopicListAdapter2.TopicListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopicListAdapter2.TopicListViewHolder {
        val itemBinding = TopicListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TopicListAdapter2.TopicListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: TopicListAdapter2.TopicListViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class TopicListViewHolder(private val itemBinding: TopicListItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {
            itemBinding.apply {
                topicTitleTv.text = data?.title
                topicTypeTv.text = data?.type
            }
        }
    }
}