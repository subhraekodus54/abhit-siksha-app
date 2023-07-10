package com.example.abithshiksha.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.YouWillLearnItemLayoutBinding
import com.example.abithshiksha.model.pojo.TestModel

class YouWillLearnAdapter(private val itemList: List<TestModel>, private val context: Context):
    RecyclerView.Adapter<YouWillLearnAdapter.YouWillLearnViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YouWillLearnAdapter.YouWillLearnViewHolder {
        val itemBinding = YouWillLearnItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return YouWillLearnAdapter.YouWillLearnViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: YouWillLearnAdapter.YouWillLearnViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class YouWillLearnViewHolder(private val itemBinding: YouWillLearnItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: TestModel, context: Context) {

            itemBinding.apply {
                //openJobAmountTv.text = "$"+data.amount_per_hour
            }
        }

    }
}