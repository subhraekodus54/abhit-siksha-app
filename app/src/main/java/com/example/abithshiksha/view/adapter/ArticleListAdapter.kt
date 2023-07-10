package com.example.abithshiksha.view.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.abithshiksha.databinding.ArticleListItemLayoutBinding
import com.example.abithshiksha.model.network.ApiConstants
import com.example.abithshiksha.model.pojo.get_article.Content
import com.example.abithshiksha.model.pojo.get_pdf.Pdf
import com.example.abithshiksha.view.activity.CourseDetailsActivity
import com.example.abithshiksha.view.activity.PdfViewerActivity
import com.example.abithshiksha.view.activity.ViewArticleActivity
import com.user.caregiver.gone
import com.user.caregiver.visible

class ArticleListAdapter(private val itemList: MutableList<Content>, private val context: Context):
    RecyclerView.Adapter<ArticleListAdapter.ArticleListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleListAdapter.ArticleListViewHolder {
        val itemBinding = ArticleListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleListAdapter.ArticleListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(jobs: List<Content>) {
        itemList.addAll(jobs)
        notifyItemInserted(itemList.size-1)
    }

    override fun onBindViewHolder(holder: ArticleListAdapter.ArticleListViewHolder, position: Int) {
        val rowData = itemList[position]
        holder.bind(rowData, context)
    }

    class ArticleListViewHolder(private val itemBinding: ArticleListItemLayoutBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Content, context: Context) {

            itemBinding.apply {
                titleTv.text = data?.title

                if(data?.content.length>50){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        descTv.text = Html.fromHtml(data?.content.toString().substring(0,50)+"...", Html.FROM_HTML_MODE_LEGACY)
                    }else{
                        descTv.text = Html.fromHtml(data?.content.toString().substring(0,50)+"...")
                    }
                }else{
                    descTv.text = Html.fromHtml(data?.content.toString()+"...")
                }

                if(data?.purchase == 1){
                    lockBtn.gone()
                    readHtv.visible()
                    rootLay.setOnClickListener {
                        val intent = Intent(context, ViewArticleActivity::class.java)
                        intent.putExtra("content", data?.content)
                        intent.putExtra("title", data?.title)
                        context.startActivity(intent)
                    }
                }else{
                    lockBtn.visible()
                    readHtv.gone()
                    rootLay.setOnClickListener {
                        Toast.makeText(context,"You have to purchase the subject to view this.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}