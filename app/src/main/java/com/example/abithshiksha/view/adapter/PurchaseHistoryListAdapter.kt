package com.example.abithshiksha.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.PurchaseHistoryItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.TopicClickListener
import com.example.abithshiksha.model.pojo.puchase_history.ResultX
import com.thekhaeng.pushdownanim.PushDownAnim

class PurchaseHistoryListAdapter(private val itemList: List<ResultX>,
                                 private val context: Context,
                                 private val topicClickListener: TopicClickListener): RecyclerView.Adapter<PurchaseHistoryListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseHistoryListAdapter.ViewHolder {
        val itemBinding = PurchaseHistoryItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PurchaseHistoryListAdapter.ViewHolder(itemBinding, topicClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: PurchaseHistoryListAdapter.ViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }
    class ViewHolder(private val itemBinding: PurchaseHistoryItemLayoutBinding, private val topicClickListener: TopicClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                priceTv.text = "₹"+data.total_amount.toString()
                boardTv.text = data?.board.toString()+" || Class"+data?.`class`.toString()
                courseTypeTv.text = data?.course_type.toString()
                dateTv.text = data?.created_at.toString()

                PushDownAnim.setPushDownAnimTo(downloadBtn).setOnClickListener {
                    topicClickListener.onTopicClick(it.rootView, data?.id)
                }
            }
        }

    }
}