package com.exp.clonefieldkonnect.adapter


import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.SalesDetailsActivity
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.helper.Constant
import com.exp.clonefieldkonnect.model.SalesModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_sales.view.*

class SalesAdapter(private var statementArr : ArrayList<SalesModel.Datum?> ) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var mcontext: Context


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    fun addData(dataViews: List<SalesModel.Datum?>) {
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_sales, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.progress_loading, parent, false)

            LoadingViewHolder(view)
        }

/*
        val v = LayoutInflater.from(mcontext).inflate(R.layout.adapter_statement, parent, false)
        return StatementHandler(v)*/
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {

            holder.itemView.tvDistributor.text = statementArr[position]!!.sellerName.toString()
            holder.itemView.tvDate.text =
                statementArr!![position]!!.invoiceDate.toString().split(" ")[0]
            holder.itemView.tvNumber.text = statementArr[position]!!.invoiceNo.toString()
            holder.itemView.tvAmount.text = statementArr[position]!!.grandTotal.toString()
            holder.itemView.tvRetailer.text = statementArr[position]!!.buyerName.toString()

            holder.itemView.relativeMainSales.tag = position

            holder.itemView.relativeMainSales.setOnClickListener { view ->
                val pos = view.tag as Int

                val intent = Intent(mcontext, SalesDetailsActivity::class.java)
                intent.putExtra("number", statementArr[pos]!!.invoiceNo)
                intent.putExtra("date", statementArr[pos]!!.invoiceDate)
                intent.putExtra("name", statementArr[pos]!!.sellerName)
                intent.putExtra("total", statementArr[pos]!!.grandTotal)
                intent.putExtra("id", statementArr[pos]!!.salesId)
                intent.putExtra("status", statementArr[pos]!!.status_id)
                intent.putExtra("statusName", statementArr[pos]!!.status_name)

                mcontext.startActivity(intent)
            }


            Glide.with(mcontext)
                .load(ApiClient.BASE_IMAGE_URL+statementArr[position]!!.invoiceImage)
                .into(holder.itemView.img)
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