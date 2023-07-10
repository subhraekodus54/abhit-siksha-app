package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.LiveClassItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.get_time_table.ResultX
import com.thekhaeng.pushdownanim.PushDownAnim

class LiveClassAdapter(private val itemList: List<ResultX>, private val context: Context):
    RecyclerView.Adapter<LiveClassAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveClassAdapter.ViewHolder {
        val itemBinding = LiveClassItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LiveClassAdapter.ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: LiveClassAdapter.ViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }
    class ViewHolder(private val itemBinding: LiveClassItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                classTv.text = data.board+" || "+data.`class`
                timeTv.text = data.class_time.toString()
                dateTv.text = data.class_date.toString()
                PushDownAnim.setPushDownAnimTo(downloadBtn).setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(data.Link)
                    context.startActivity(i)
                }
            }
        }

    }
}