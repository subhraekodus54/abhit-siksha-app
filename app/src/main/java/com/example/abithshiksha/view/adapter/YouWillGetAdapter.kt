package com.example.abithshiksha.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.YouWillGetItemLayoutBinding
import com.example.abithshiksha.model.pojo.TestModel

class YouWillGetAdapter  (private val itemList: List<TestModel>, private val context: Context):
    RecyclerView.Adapter<YouWillGetAdapter.YouWillGetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouWillGetAdapter.YouWillGetViewHolder {
        val itemBinding = YouWillGetItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return YouWillGetAdapter.YouWillGetViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: YouWillGetAdapter.YouWillGetViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class YouWillGetViewHolder(private val itemBinding: YouWillGetItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: TestModel, context: Context) {

            itemBinding.apply {
                //openJobAmountTv.text = "$"+data.amount_per_hour
                Glide.with(context)
                    .load(data?.name) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(imgView)
            }
        }

    }
}