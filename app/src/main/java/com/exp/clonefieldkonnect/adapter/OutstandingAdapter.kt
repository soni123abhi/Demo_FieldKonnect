package com.exp.clonefieldkonnect.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.databinding.AdapterOutstandingBinding
import com.exp.clonefieldkonnect.databinding.ProgressLoadingBinding
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.OutstandingModel

class OutstandingAdapter(
    private var statementArr: ArrayList<OutstandingModel.Data?>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var mContext: Context

    // Item ViewHolder using ViewBinding
    class ItemViewHolder(val binding: AdapterOutstandingBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Loading ViewHolder using ViewBinding
    class LoadingViewHolder(val binding: ProgressLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun addData(dataViews: List<OutstandingModel.Data?>) {
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
        if (statementArr.size != 0) {
            statementArr.removeAt(statementArr.size - 1)
            notifyItemRemoved(statementArr.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context

        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val binding = AdapterOutstandingBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
            ItemViewHolder(binding)
        } else {
            val binding = ProgressLoadingBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
            LoadingViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = statementArr[position] ?: return

            with(holder.binding) {
                tvRefNo.text = item.reference_no ?: ""
                tvType.text = item.payment_type ?: ""
                tvMode.text = (item.payment_mode ?: "").toString()
                tvDate.text = item.payment_date ?: ""
                tvAmount.text = item.amount ?: ""

                relativeMainSales.setOnClickListener {
                    // handle click here if needed
                }
            }
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
