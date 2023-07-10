package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.SelectSubjectItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_cart_details.CartSubjectDetail
import com.example.abithshiksha.view.activity.CourseDetailsActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class CartSubjectAdapter(private val itemList: List<CartSubjectDetail>,
                         private val context: Context,
                         private val board: String,
                         private val standard: String):
    RecyclerView.Adapter<CartSubjectAdapter.CartSubjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartSubjectAdapter.CartSubjectViewHolder {
        val itemBinding = SelectSubjectItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartSubjectAdapter.CartSubjectViewHolder(itemBinding, board, standard)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartSubjectAdapter.CartSubjectViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class CartSubjectViewHolder(private val itemBinding: SelectSubjectItemLayoutBinding, private val board: String, private val standard: String) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: CartSubjectDetail, context: Context) {

            itemBinding.apply {
                selectImg.gone()
                selectLot.gone()
                purchasedHtv.gone()
                boardTv.text = data?.subject_name+" || "+board+" || "+"Class "+standard
                priceTv.text = "₹"+data?.amount.toString()
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(itemImg)

                viewImg.setOnClickListener {
                    val intent = Intent(context, CourseDetailsActivity::class.java)
                    intent.putExtra("id", data?.assign_subject_id)
                    context.startActivity(intent)
                }

                if(data?.is_available == 0){
                    purchasedHtv.visible()
                    purchasedHtv.text = "Inactive"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        purchasedHtv.setTextColor(context.resources.getColor(R.color.error_red, null))
                    }
                }else{
                    purchasedHtv.gone()
                }

                /*if(alreadyPurchased){
                    purchasedHtv.visible()
                }else{
                    purchasedHtv.gone()
                }*/
            }
        }

    }
}