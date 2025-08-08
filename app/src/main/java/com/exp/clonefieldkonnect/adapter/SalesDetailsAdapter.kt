package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.SalesDetailModel
import java.text.DecimalFormat

class SalesDetailsAdapter(
    val context: Activity,
    val orderDetails: ArrayList<SalesDetailModel.Saledetails>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.adapter_sales_order_details, parent, false)
        return StatementHandler(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

        if(orderDetails!!.size== (position+1)){
            statementHandler.view.visibility = View.GONE
        }else{
            statementHandler.view.visibility = View.VISIBLE
        }

        statementHandler.tvName.text = orderDetails!![position]!!.products!!.productName.toString()
        statementHandler.tvQuantity.text = orderDetails!![position]!!.quantity.toString()
        statementHandler.tvRate.text = orderDetails!![position]!!.price.toString()
        statementHandler.tv_brand_name.text = "Brand :- "+orderDetails!![position]!!.products!!.brands!!.brandName.toString()
        statementHandler.tv_model.text = "Model No :- "+orderDetails!![position]!!.products!!.model_no.toString()
        statementHandler.tvAmount.text = DecimalFormat("##.#").format(orderDetails?.get(position)?.lineTotal.toString().toFloat()).toString()
    }


    override fun getItemCount(): Int {
        return orderDetails!!.size
    }

    private inner class StatementHandler internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tv_brand_name: TextView = itemView.findViewById(R.id.tv_brand_name)
        var tv_model: TextView = itemView.findViewById(R.id.tv_model)
        var tvQuantity: TextView = itemView.findViewById(R.id.tvQuantity)
        var tvRate: TextView = itemView.findViewById(R.id.tvRate)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var view: View = itemView.findViewById(R.id.view)

    }
}