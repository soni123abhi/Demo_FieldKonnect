package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.DealerSalesReportModel

class DealerSalesReportAdapter(var activity: Activity, var dealersaleslist: ArrayList<DealerSalesReportModel.Data>, var onClickEmail_expense: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_dealerslelayout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        if (dealersaleslist[position].dealer!!.isNotEmpty()){
            statementHandler.title_name_value.text = dealersaleslist[position].dealer
        }else{
            statementHandler.title_name_value.text = "-"
        }

        if (dealersaleslist[position].totalNetAmountLastYear!!.isNotEmpty()){
            statementHandler.title_ly_value.text = dealersaleslist[position].totalNetAmountLastYear
        }else{
            statementHandler.title_ly_value.text = "-"
        }

        if (dealersaleslist[position].totalNetAmountLastMonth!!.isNotEmpty()){
            statementHandler.title_lmtd_value.text = dealersaleslist[position].totalNetAmountLastMonth
        }else{
            statementHandler.title_lmtd_value.text = "-"
        }

        if (dealersaleslist[position].totalNetAmountCurrentYear!!.isNotEmpty()){
            statementHandler.title_cy_value.text = dealersaleslist[position].totalNetAmountCurrentYear
        }else{
            statementHandler.title_cy_value.text = "-"
        }

        if (dealersaleslist[position].totalNetAmountCurrentMonth!!.isNotEmpty()){
            statementHandler.title_cmtd_value.text = dealersaleslist[position].totalNetAmountCurrentMonth
        }else{
            statementHandler.title_cmtd_value.text = "-"
        }

        statementHandler.title_name_value.setOnClickListener {
            onClickEmail_expense.onClickEmail_expense(dealersaleslist[position].dealer!!)
        }


    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var title_name_value: TextView = itemView.findViewById(R.id.title_name_value)
        var title_ly_value: TextView = itemView.findViewById(R.id.title_ly_value)
        var title_lmtd_value: TextView = itemView.findViewById(R.id.title_lmtd_value)
        var title_cy_value: TextView = itemView.findViewById(R.id.title_cy_value)
        var title_cmtd_value: TextView = itemView.findViewById(R.id.title_cmtd_value)
    }

    override fun getItemCount(): Int {
        return dealersaleslist.size
    }

    interface OnEmailClick {
        fun onClickEmail_expense(name: String)
    }


}