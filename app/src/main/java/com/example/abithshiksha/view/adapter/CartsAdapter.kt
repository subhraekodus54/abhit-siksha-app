package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.MainActivity
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.CartItemLayoutBinding
import com.example.abithshiksha.helper.click_listener.SubjectClickListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_carts.Cart
import com.example.abithshiksha.view.activity.CartDetailsActivity

class CartsAdapter(private val itemList: List<Cart>, private val context: Context, private val subjectClickListener: SubjectClickListener):
    RecyclerView.Adapter<CartsAdapter.CartsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartsAdapter.CartsViewHolder {
        val itemBinding = CartItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartsAdapter.CartsViewHolder(itemBinding,subjectClickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartsAdapter.CartsViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class CartsViewHolder(private val itemBinding: CartItemLayoutBinding, private val subjectClickListener: SubjectClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Cart, context: Context) {

            itemBinding.apply {
                boardTv.text = data?.board
                classTv.text = "Class "+data?.assign_class
                if(data?.course_type == "1"){
                    cTypeTv.text = "Full"
                    rootLay.setBackgroundResource(R.drawable.cart_item_bg)
                }else{
                    cTypeTv.text = "Custom"
                    rootLay.setBackgroundResource(R.drawable.cart_item_bg_2)
                }
                amountTv.text = "₹"+data?.cart_total_amount.toString()
                if(data?.assign_subject.length>20){
                    subjectsTv.text = data?.assign_subject.toString().substring(0,20)+"..."
                }else{
                    subjectsTv.text = data?.assign_subject.toString()
                }
                goTv.setOnClickListener {
                    val intent = Intent(context, CartDetailsActivity::class.java)
                    intent.putExtra("id",data?.id)
                    context.startActivity(intent)
                }
                removeCartTv.setOnClickListener {
                    subjectClickListener.onClick(it.rootView, data?.id)
                }

                if(data?.total_subject>1){
                    totalSubCountTv.text = data?.total_subject.toString()+" Subjects"
                }else{
                    totalSubCountTv.text = data?.total_subject.toString()+" Subject"
                }
            }
        }
    }
}