package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abithshiksha.R
import com.example.abithshiksha.databinding.SelectSubjectItemLayoutBinding
import com.example.abithshiksha.helper.CourseSelectListener
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.TestModel
import com.example.abithshiksha.model.pojo.get_filtered_subject.Subject
import com.example.abithshiksha.view.activity.CourseDetailsActivity
import com.example.abithshiksha.view.fragment.CoursesFragment
import com.user.caregiver.gone
import com.user.caregiver.visible

class SelectSubjectAdapter(private val itemList: List<Subject>,
                           private val context: Context,
                           private val isCustom: Boolean,
                           private val courseSelectListener: CourseSelectListener,
                           private val subject_id: Int,
                           private val isEnroll: Boolean):
    RecyclerView.Adapter<SelectSubjectAdapter.SelectSubjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectSubjectAdapter.SelectSubjectViewHolder {
        val itemBinding = SelectSubjectItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectSubjectAdapter.SelectSubjectViewHolder(itemBinding,isCustom,subject_id,isEnroll,courseSelectListener)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: SelectSubjectAdapter.SelectSubjectViewHolder, position: Int) {

        val rowData = itemList[position]
        holder.bind(rowData, context)
    }
    class SelectSubjectViewHolder(private val itemBinding: SelectSubjectItemLayoutBinding,
                                  private val isCustom: Boolean,
                                  private val subject_id: Int,
                                  private val isEnroll: Boolean,
                                  private val courseSelectListener: CourseSelectListener) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Subject, context: Context) {

            itemBinding.apply {
                priceTv.text = "₹"+data?.subject_amount.toString()
                boardTv.text = data?.subject_name
                Glide.with(context)
                    .load(ApiConstants.PUBLIC_URL+data?.image) // image url
                    .placeholder(R.drawable.error_img_bg) // any placeholder to load at start
                    .centerCrop()
                    .into(itemImg)

                if(data?.already_purchase == 1){
                    selectImg.gone()
                    selectLot.gone()
                    purchasedHtv.visible()
                }else{
                    purchasedHtv.gone()
                    if(isCustom == true){
                        if(isEnroll == true){
                            if(data?.id == subject_id){
                                selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24))
                                selectImg.visible()
                                selectLot.gone()
                                Log.e("custom","here 1")

                                var isClicked: Boolean = true
                                selectImg.setOnClickListener {
                                    if(isClicked == false){
                                        isClicked = true
                                        selectLot.gone()
                                        selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24))
                                        courseSelectListener.onSelect(it,data?.subject_amount, true, data?.id)
                                    }else{
                                        isClicked = false
                                        selectLot.visible()
                                        selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_border_bg))
                                        courseSelectListener.onSelect(it,data?.subject_amount, false, data?.id)
                                    }
                                }


                            }else{
                                Log.e("custom","here 2   =>"+data?.id+" == "+subject_id)

                                selectLot.visible()
                                selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_border_bg))
                                var isClicked: Boolean = false
                                selectImg.setOnClickListener {
                                    if(isClicked == false){
                                        isClicked = true
                                        selectLot.gone()
                                        selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24))
                                        courseSelectListener.onSelect(it,data?.subject_amount, true, data?.id)
                                    }else{
                                        isClicked = false
                                        selectLot.visible()
                                        selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_border_bg))
                                        courseSelectListener.onSelect(it,data?.subject_amount, false, data?.id)
                                    }
                                }
                            }
                        }else{
                            Log.e("custom","here 3")

                            selectLot.visible()
                            selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_border_bg))
                            var isClicked: Boolean = false
                            selectImg.setOnClickListener {
                                if(isClicked == false){
                                    isClicked = true
                                    selectLot.gone()
                                    selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24))
                                    courseSelectListener.onSelect(it,data?.subject_amount, true, data?.id)
                                }else{
                                    isClicked = false
                                    selectLot.visible()
                                    selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.circle_border_bg))
                                    courseSelectListener.onSelect(it,data?.subject_amount, false, data?.id)
                                }
                            }
                        }
                    }else{
                        selectImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_check_circle_24))
                        selectImg.visible()
                        selectLot.gone()
                    }
                }

                viewImg.setOnClickListener {
                    val intent = Intent(context, CourseDetailsActivity::class.java)
                    intent.putExtra("id", data?.id)
                    context.startActivity(intent)
                }

            }
        }

    }
}