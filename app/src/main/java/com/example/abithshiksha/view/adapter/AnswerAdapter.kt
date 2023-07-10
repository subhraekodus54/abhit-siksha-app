package com.example.abithshiksha.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.AnswereItemLayoutBinding

class AnswerAdapter(private val itemList: MutableList<String>, private val context: Context):
    RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerAdapter.AnswerViewHolder {
        val itemBinding = AnswereItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnswerAdapter.AnswerViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<String>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: AnswerAdapter.AnswerViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context, position)
    }

    class AnswerViewHolder(private val itemBinding: AnswereItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: String, context: Context, position: Int) {
            itemBinding.apply {
                answerTv.text = data
                var isClicked:Boolean = false

                rootLay.setOnClickListener {
                    Log.e("pos",position.toString())
                    if(isClicked == false){
                        isClicked = true
                        answerLay.background = ContextCompat.getDrawable(context, R.color.orange)
                    }else{
                        isClicked = false
                        answerLay.background = ContextCompat.getDrawable(context, R.color.white)
                    }
                }
            }
        }
    }
}