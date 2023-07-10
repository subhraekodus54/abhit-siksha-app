package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.McqSetsItemLayoutBinding
import com.example.abithshiksha.model.pojo.get_mcq_sets.ResultX
import com.example.abithshiksha.view.activity.McqResultActivity
import com.example.abithshiksha.view.activity.McqSetsActivity
import com.example.abithshiksha.view.activity.TestPlayActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class McqSetsAdapter(private val itemList: MutableList<ResultX>, private val context: Context):
    RecyclerView.Adapter<McqSetsAdapter.McqSetsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): McqSetsAdapter.McqSetsViewHolder {
        val itemBinding = McqSetsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return McqSetsAdapter.McqSetsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<ResultX>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: McqSetsAdapter.McqSetsViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class McqSetsViewHolder(private val itemBinding: McqSetsItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: ResultX, context: Context) {

            itemBinding.apply {
                testNameTv.text = data?.name
                mcqCountTv.text = data?.total_question.toString()+" questions"
                playBtn.setOnClickListener {
                    val intent = Intent(context, TestPlayActivity::class.java)
                    intent.putExtra("set_id",data?.id)
                    context.startActivity(intent)
                }
                analyseBtn.setOnClickListener {
                    val intent = Intent(context, McqResultActivity::class.java)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
                if(data?.is_played == 1){
                    analyseBtn.visible()
                }else{
                    analyseBtn.gone()
                }
            }
        }

    }
}