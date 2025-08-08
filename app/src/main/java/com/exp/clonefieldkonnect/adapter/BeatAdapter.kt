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
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.BeatModel
import kotlinx.android.synthetic.main.adapter_beat.view.*

class BeatAdapter(val arr : java.util.ArrayList<BeatModel?>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_beat, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {

          holder.itemView.tvBeatName.text = arr[position]!!.beatName

          holder.itemView.tvTotal.text = arr[position]!!.total_customers.toString()
          holder.itemView.tvVisited.text = arr[position]!!.visited_customers.toString()
          holder.itemView.tvRemaining.text = arr[position]!!.remaining_customers.toString()

            if(arr[position]!!.is_today){
                holder.itemView.viewShade1.visibility = View.GONE
                holder.itemView.viewShade.visibility = View.GONE
                holder.itemView.cardBeat.setCardBackgroundColor(Color.parseColor("#ffffff"))
                holder.itemView.cardBeat.cardElevation = 4f
            }else{
                holder.itemView.viewShade1.visibility = View.VISIBLE
                holder.itemView.viewShade.visibility = View.VISIBLE
                holder.itemView.cardBeat.setCardBackgroundColor(Color.parseColor("#FFFEF8"))
                holder.itemView.cardBeat.cardElevation = 2f
            }

            holder.itemView.cardBeat.tag = position
            holder.itemView.cardBeat.setOnClickListener { view ->
                val pos= view.tag as Int
                if(arr[pos]!!.is_today) {
//                    Toast.makeText(mcontext,"Working on it..!!",Toast.LENGTH_SHORT).show()
                    val intent = Intent(mcontext, BeatCustomerActivity::class.java)
                    intent.putExtra("id", arr[pos]!!.beatId.toString())
                    intent.putExtra("name", arr[pos]!!.beatName.toString())
                    intent.putExtra("is_today", arr[pos]!!.is_today)
                    if (arr[pos]!!.beatscheduleid != null)
                        intent.putExtra("beat_schedule_id", arr[pos]!!.beatscheduleid.toString())
                    else
                        intent.putExtra("beat_schedule_id", "")
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