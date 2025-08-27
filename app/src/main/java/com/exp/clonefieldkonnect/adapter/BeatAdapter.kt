package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.BeatCustomerActivity
import com.exp.clonefieldkonnect.databinding.AdapterBeatBinding
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.BeatModel

class BeatAdapter(val arr : java.util.ArrayList<BeatModel?>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
//    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ItemViewHolder(val binding: AdapterBeatBinding) : RecyclerView.ViewHolder(binding.root)


    fun addData(dataViews: List<BeatModel>) {
        this.arr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            arr.add(null)
            notifyItemInserted(arr.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (arr.size != 0) {
            arr.removeAt(arr.size - 1)
            notifyItemRemoved(arr.size)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context


        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val binding = AdapterBeatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemViewHolder(binding)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val beat = arr[position]!!

            holder.binding.tvBeatName.text = beat.beatName
            holder.binding.tvTotal.text = beat.total_customers.toString()
            holder.binding.tvVisited.text = beat.visited_customers.toString()
            holder.binding.tvRemaining.text = beat.remaining_customers.toString()

            if (beat.is_today) {
                holder.binding.viewShade1.visibility = View.GONE
                holder.binding.viewShade.visibility = View.GONE
                holder.binding.cardBeat.setCardBackgroundColor(Color.parseColor("#ffffff"))
                holder.binding.cardBeat.cardElevation = 4f
            } else {
                holder.binding.viewShade1.visibility = View.VISIBLE
                holder.binding.viewShade.visibility = View.VISIBLE
                holder.binding.cardBeat.setCardBackgroundColor(Color.parseColor("#FFFEF8"))
                holder.binding.cardBeat.cardElevation = 2f
            }

            holder.binding.cardBeat.setOnClickListener {
                if (beat.is_today) {
                    val intent = Intent(mcontext, BeatCustomerActivity::class.java).apply {
                        putExtra("id", beat.beatId.toString())
                        putExtra("name", beat.beatName.toString())
                        putExtra("is_today", beat.is_today)
                        putExtra("beat_schedule_id", beat.beatscheduleid?.toString() ?: "")
                    }
                    mcontext.startActivity(intent)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (arr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return arr.size
    }

}