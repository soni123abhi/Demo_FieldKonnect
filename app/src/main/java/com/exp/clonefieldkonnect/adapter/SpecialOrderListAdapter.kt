package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.activity.OrderDetailEditActivity
import com.exp.clonefieldkonnect.model.SpecialDiscountModel
import kotlin.collections.ArrayList

class SpecialOrderListAdapter(var activity: Activity, var orderListArr: ArrayList<SpecialDiscountModel.Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var flag = ""
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_order_history, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tvOrderID.text = orderListArr.get(position)!!.orderId.toString()
        statementHandler.tvDate.text = orderListArr.get(position)!!.orderDate.toString()
        statementHandler.tvFirmName.text = orderListArr.get(position)!!.buyerName.toString()
        statementHandler.tvQty.text = orderListArr.get(position)!!.totalQty.toString()
        statementHandler.tvStatus.text = orderListArr.get(position)!!.discountStatus.toString()

        if(orderListArr[position]!!.grandTotal.toString().contains(".")){
            statementHandler.tvAmount.text = orderListArr.get(position)!!.grandTotal.toString().split(".")[0].toString()
        }else{
            statementHandler.tvAmount.text = orderListArr.get(position)!!.grandTotal.toString()
        }

        statementHandler.relativeMainSales.setOnClickListener {
            val pos = position
            println("ABHGDGDGDGD="+orderListArr[pos]!!.orderId)
            val intent = Intent( activity, OrderDetailEditActivity::class.java)
            intent.putExtra("id",orderListArr[pos]!!.orderId)
            intent.putExtra("name",orderListArr[pos]!!.buyerName)
            intent.putExtra("no",orderListArr[pos]!!.orderno)
            intent.putExtra("Status",orderListArr[pos]!!.discountStatus)
            intent.putExtra("type","special")
            activity.startActivity(intent)
        }



        when (orderListArr[position]!!.discountStatus.toString()) {
            "Approved" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#00D23B"))
            }
            "Reject" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#FF0000"))
            }
            "Pending" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#FFC700"))
            }
        }
    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvOrderID: TextView = itemView.findViewById(R.id.tvOrderID)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        var tvFirmName: TextView = itemView.findViewById(R.id.tvFirmName)
        var tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        var tvQty: TextView = itemView.findViewById(R.id.tvQty)
        var relativeMainSales: RelativeLayout = itemView.findViewById(R.id.relativeMainSales)
    }

    override fun getItemCount(): Int {
        return orderListArr.size
    }

}