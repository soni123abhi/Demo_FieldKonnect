package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.OutstandingModel
import kotlinx.android.synthetic.main.adapter_outstanding.view.*
import kotlinx.android.synthetic.main.adapter_sales.view.relativeMainSales
import kotlinx.android.synthetic.main.adapter_sales.view.tvAmount
import kotlinx.android.synthetic.main.adapter_sales.view.tvDate

class OutstandingAdapter(private var statementArr : ArrayList<OutstandingModel.Data?> ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    lateinit var mcontext: Context


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addData(dataViews: List<OutstandingModel.Data?>) {
        this.statementArr.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        //add loading item
        Handler().post {
            statementArr.add(null)
            notifyItemInserted(statementArr.size - 1)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (statementArr.size != 0) {
            statementArr.removeAt(statementArr.size - 1)
            notifyItemRemoved(statementArr.size)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context

        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_outstanding, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)

            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {

            holder.itemView.tvRefNo.text = statementArr[position]!!.reference_no.toString()
            holder.itemView.tvType.text = statementArr[position]!!.payment_type.toString()
            holder.itemView.tvMode.text = statementArr[position]!!.payment_mode.toString()
            holder.itemView.tvDate.text =
                statementArr!![position]!!.payment_date.toString()
              holder.itemView.tvAmount.text = statementArr[position]!!.amount.toString()

            holder.itemView.relativeMainSales.tag = position
            holder.itemView.relativeMainSales.setOnClickListener { view ->

              /*  val pos = view.tag as Int
                val intent = Intent( mcontext, OrderDetailsActivity::class.java)
                intent.putExtra("id",statementArr[pos]!!.orderId)
                intent.putExtra("name",statementArr[pos]!!.buyerName)
                intent.putExtra("no",statementArr[pos]!!.orderno)
                mcontext.startActivity(intent)*/

            }
        }
    }


    override fun getItemCount(): Int {
        return statementArr!!.size

    }

    override fun getItemViewType(position: Int): Int {
        return if (statementArr[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }



}