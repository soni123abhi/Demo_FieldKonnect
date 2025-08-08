package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.model.SalesImageModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_image.view.*

class SalesImageAdapter(val arr : ArrayList<SalesImageModel>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context
    val arrInt : ArrayList<Int> = arrayListOf()
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        mcontext = parent.context

        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_image, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        Glide.with(mcontext).load(ApiClient.BASE_IMAGE_URL+arr[position].file_path).into(holder.itemView.img)

        holder.itemView.relativeDelete.visibility = View.GONE
    }


    override fun getItemCount(): Int {
        return arr.size
    }

}