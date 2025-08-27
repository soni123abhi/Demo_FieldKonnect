package com.exp.clonefieldkonnect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.databinding.AdapterImageBinding
import com.exp.clonefieldkonnect.model.SalesImageModel

class SalesImageAdapter(private val arr: ArrayList<SalesImageModel>) :
    RecyclerView.Adapter<SalesImageAdapter.ItemViewHolder>() {

    private lateinit var mContext: Context

    inner class ItemViewHolder(val binding: AdapterImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        val binding = AdapterImageBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = arr[position]

        Glide.with(mContext)
            .load(ApiClient.BASE_IMAGE_URL + item.file_path)
            .into(holder.binding.img)

        holder.binding.relativeDelete.visibility = android.view.View.GONE
    }

    override fun getItemCount(): Int = arr.size
}
