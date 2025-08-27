package com.exp.clonefieldkonnect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.databinding.AdapterImageBinding

class ImageAdapter(private val arr: ArrayList<String>) :
    RecyclerView.Adapter<ImageAdapter.ItemViewHolder>() {

    private lateinit var mContext: Context
    private val arrInt: ArrayList<Int> = arrayListOf()

    // ViewHolder uses ViewBinding
    class ItemViewHolder(val binding: AdapterImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        mContext = parent.context
        val binding = AdapterImageBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val imageUrl = ApiClient.BASE_IMAGE_URL + arr[position]

        Glide.with(mContext)
            .load(imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // optional
            .into(holder.binding.img)

        holder.binding.imgDelete.setOnClickListener {
            arr.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, arr.size)
            arrInt.add(position)
        }
    }

    fun deletePos(): ArrayList<Int> {
        return arrInt
    }

    override fun getItemCount(): Int = arr.size
}
