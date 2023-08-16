package com.example.abithshiksha.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.AddOnsItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.AddOnsClickListener
import com.example.abithshiksha.model.pojo.add_ons.Addon

class AddOnsListAdapter(private val itemList: MutableList<Addon>,
                        private val context: Context,
                        private val addOnsClickListener: AddOnsClickListener
    ):
    RecyclerView.Adapter<AddOnsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddOnsListAdapter.ViewHolder {
        val itemBinding = AddOnsItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddOnsListAdapter.ViewHolder(itemBinding, addOnsClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: AddOnsListAdapter.ViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context, position)
    }

    class ViewHolder(private val itemBinding: AddOnsItemLayoutBinding, private val addOnsClickListener: AddOnsClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Addon, context: Context, position: Int) {
            itemBinding.apply {
                addOnItemNameTv.text = data.name
                priceTv.text = "₹${data.price}"
                checkBox.isChecked = data.isSelected == true
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    addOnsClickListener.onClick(buttonView.rootView, data.id, isChecked, data.price.toInt())
                }
            }
        }
    }
}