package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.PdfListItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_lessons.Data
import com.example.abithshiksha.model.pojo.get_pdf.Pdf
import com.example.abithshiksha.view.activity.PdfViewerActivity
import com.example.abithshiksha.view.activity.VideoPlayActivity
import com.example.abithshiksha.view.activity.ViewArticleActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class PdfListAdapter(private val itemList: MutableList<Pdf>,
                     private val context: Context
):
    RecyclerView.Adapter<PdfListAdapter.PdfListViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfListAdapter.PdfListViewHolder {
        val itemBinding = PdfListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PdfListAdapter.PdfListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<Pdf>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: PdfListAdapter.PdfListViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class PdfListViewHolder(private val itemBinding: PdfListItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Pdf, context: Context) {

            itemBinding.apply {
                titleTv.text = data?.title

                if(data?.purchase == 1){
                    lockBtn.gone()
                    arrow.visible()
                    rootLay.setOnClickListener {
                        val intent = Intent(context, PdfViewerActivity::class.java)
                        intent.putExtra("url", ApiConstants.PUBLIC_URL+data?.pdf_url)
                        intent.putExtra("name", data?.pdf_name)
                        context.startActivity(intent)
                    }
                }else{
                    lockBtn.visible()
                    arrow.gone()
                    rootLay.setOnClickListener {
                        Toast.makeText(context,"You have to purchase the subject to view this.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}