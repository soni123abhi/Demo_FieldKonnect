package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.PrimarySchemeTableModel

class SchemeDataShowTableAdapter(var activity: Activity, var schemetabledatashow: ArrayList<PrimarySchemeTableModel.Data>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_primaryschemelayout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler

//        statementHandler.title_ly_value.setBackgroundColor(Color.WHITE)
//        statementHandler.title_lmtd_value.setBackgroundColor(Color.WHITE)
//        statementHandler.title_cy_value.setBackgroundColor(Color.WHITE)
//        statementHandler.title_cmtd_value.setBackgroundColor(Color.WHITE)

        if (schemetabledatashow[position].dealerName!!.isNotEmpty()){
            statementHandler.title_name_value.text = schemetabledatashow[position].dealerName
        }else{
            statementHandler.title_name_value.text = "-"
        }

        if (schemetabledatashow[position].groupName!!.isNotEmpty()){
            statementHandler.title_ly_value.text = schemetabledatashow[position].groupName
        }else{
            statementHandler.title_ly_value.text = "-"
        }

        if (schemetabledatashow[position].saleQty!!.isNotEmpty()){
            statementHandler.title_lmtd_value.text = schemetabledatashow[position].saleQty
        }else{
            statementHandler.title_lmtd_value.text = "-"
        }

        if (schemetabledatashow[position].saleAmount!!.isNotEmpty()){
            statementHandler.title_cy_value.text = schemetabledatashow[position].saleAmount
        }else{
            statementHandler.title_cy_value.text = "-"
        }

        if (schemetabledatashow[position].discountCn!!.toString().isNotEmpty()){
            statementHandler.title_cmtd_value.text = schemetabledatashow[position].discountCn.toString()
        }else{
            statementHandler.title_cmtd_value.text = "-"
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
        return schemetabledatashow.size
    }



}