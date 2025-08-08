package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.CustomerActivityModel

class CustomerActivityAdapter(var activity: Activity, var customeractivityList: ArrayList<CustomerActivityModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.customer_detail_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tv_tittle.setText(customeractivityList[position].reportTitle)
        statementHandler.tv_name.setText(customeractivityList[position].user_name)
        statementHandler.tv_msg.setText(customeractivityList[position].description)
        statementHandler.tv_date.setText(customeractivityList[position].createdAt)

    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tv_tittle: TextView = itemView.findViewById(R.id.tv_tittle)
        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var tv_msg: TextView = itemView.findViewById(R.id.tv_msg)
        var tv_date: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun getItemCount(): Int {
        return customeractivityList.size
    }

}