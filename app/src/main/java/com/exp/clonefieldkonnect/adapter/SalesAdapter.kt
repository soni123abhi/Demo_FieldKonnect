package com.exp.clonefieldkonnect.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.activity.SalesDetailsActivity
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.databinding.AdapterSalesBinding
import com.exp.clonefieldkonnect.databinding.ProgressLoadingBinding
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.SalesModel

class SalesAdapter(
    private var statementArr: ArrayList<SalesModel.Datum?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class ItemViewHolder(val binding: AdapterSalesBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(val binding: ProgressLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun addData(dataViews: List<SalesModel.Datum?>) {
        this.statementArr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        Handler().post {
            statementArr.add(null)
            notifyItemInserted(statementArr.size - 1)
        }
    }

    fun removeLoadingView() {
        if (statementArr.isNotEmpty()) {
            statementArr.removeAt(statementArr.size - 1)
            notifyItemRemoved(statementArr.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val binding = AdapterSalesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val binding = ProgressLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val data = statementArr[position] ?: return

            holder.binding.tvDistributor.text = data.sellerName
            holder.binding.tvDate.text = data.invoiceDate?.split(" ")?.getOrNull(0) ?: ""
            holder.binding.tvNumber.text = data.invoiceNo
            holder.binding.tvAmount.text = data.grandTotal
            holder.binding.tvRetailer.text = data.buyerName

            holder.binding.relativeMainSales.setOnClickListener {
                val intent = Intent(mcontext, SalesDetailsActivity::class.java).apply {
                    putExtra("number", data.invoiceNo)
                    putExtra("date", data.invoiceDate)
                    putExtra("name", data.sellerName)
                    putExtra("total", data.grandTotal)
                    putExtra("id", data.salesId)
                    putExtra("status", data.status_id)
                    putExtra("statusName", data.status_name)
                }
                mcontext.startActivity(intent)
            }

            Glide.with(mcontext)
                .load(ApiClient.BASE_IMAGE_URL + data.invoiceImage)
                .into(holder.binding.img)
        }
    }

    override fun getItemCount(): Int = statementArr.size

    override fun getItemViewType(position: Int): Int {
        return if (statementArr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }
}
