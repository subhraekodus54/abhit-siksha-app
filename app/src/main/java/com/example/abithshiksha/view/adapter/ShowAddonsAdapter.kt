package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.ShowAddOnsItemLayBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_addons.SelectedAddon
import com.example.abithshiksha.view.activity.PdfViewerActivity
import com.example.abithshiksha.view.activity.VideoPlayActivity

class ShowAddonsAdapter(private val itemList: List<SelectedAddon>,
                        private val context: Context,
    ):
    RecyclerView.Adapter<ShowAddonsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowAddonsAdapter.ViewHolder {
        val itemBinding = ShowAddOnsItemLayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ShowAddonsAdapter.ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class ViewHolder(private val itemBinding: ShowAddOnsItemLayBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: SelectedAddon, context: Context) {

            itemBinding.apply {
                titleTv.text = data.selected_addon.name

                if(data.selected_addon.type == "image"){
                    contentImg.setImageResource(R.drawable.baseline_insert_drive_file_24)
                }else if(data.selected_addon.type == "video"){
                    contentImg.setImageResource(R.drawable.baseline_ondemand_video_24)
                }

                rootLay.setOnClickListener {
                    if(data.selected_addon.type == "pdf"){
                        val intent = Intent(context, PdfViewerActivity::class.java)
                        intent.putExtra("url", ApiConstants.PUBLIC_URL+data?.selected_addon.file_path)
                        intent.putExtra("name", data?.selected_addon.name)
                        context.startActivity(intent)
                    }else if(data.selected_addon.type == "image"){

                    }else if(data.selected_addon.type == "video"){
                        val intent = Intent(context, VideoPlayActivity::class.java)
                        intent.putExtra("url",data?.selected_addon.file_path)
                        intent.putExtra("id","csfdtChegbddvdh")
                        intent.putExtra("user_id", data?.user_id)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}