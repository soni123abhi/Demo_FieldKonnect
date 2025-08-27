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
import com.exp.clonefieldkonnect.activity.OrderDetailsActivity
import com.exp.clonefieldkonnect.model.OrderListModel
import kotlin.collections.ArrayList

class OrderListNewAdapter(var activity: Activity, var orderListArr: ArrayList<OrderListModel.Datum?>) :
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
        statementHandler.tvStatus.text = orderListArr.get(position)!!.order_status.toString()
        statementHandler.tvcreatedby.text = orderListArr.get(position)!!.creatd_by.toString()

        if(orderListArr[position]!!.subTotal.toString().contains(".")){
            statementHandler.tvAmount.text = orderListArr.get(position)!!.subTotal.toString().split(".")[0].toString()
        }else{
            statementHandler.tvAmount.text = orderListArr.get(position)!!.subTotal.toString()
        }

        statementHandler.relativeMainSales.setOnClickListener {
            val pos = position
            println("ABHGDGDGDGD="+orderListArr[pos]!!.orderId)
            val intent = Intent( activity, OrderDetailsActivity::class.java)
            intent.putExtra("id",orderListArr[pos]!!.orderId)
            intent.putExtra("name",orderListArr[pos]!!.buyerName)
            intent.putExtra("no",orderListArr[pos]!!.orderno)
            activity.startActivity(intent)
        }



        when (orderListArr[position]!!.order_status_id.toString()) {
            "1" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#00D23B"))
            }
            "4" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#FF0000"))
            }
            "0" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#FFC700"))
            }
            "2" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#813F0B"))
            }
            "3" -> {
                statementHandler.tvStatus.setTextColor(Color.parseColor("#DF8F18"))
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
        var tvcreatedby: TextView = itemView.findViewById(R.id.tvcreatedby)
        var relativeMainSales: RelativeLayout = itemView.findViewById(R.id.relativeMainSales)
    }

    override fun getItemCount(): Int {
        return orderListArr.size
    }

}