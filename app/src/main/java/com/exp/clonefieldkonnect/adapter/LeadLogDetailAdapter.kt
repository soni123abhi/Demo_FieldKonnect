package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.LeadCallLogDetailModel

class LeadLogDetailAdapter(var activity: Activity, var useractivitylist: ArrayList<LeadCallLogDetailModel.Data_log>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_callhistroy_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        statementHandler.user_name.text = item.contactName.toString()
        statementHandler.user_mob_no.text = item.number.orEmpty()

        if (item.status == 0){
            statementHandler.relll_durr.visibility = View.GONE
            statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FFE3DD"))
            statementHandler.tv_lead_status.text = "No Response"
            statementHandler.tv_lead_status.setTextColor(Color.parseColor("#4D2F28"))

        }else{
            statementHandler.relll_durr.visibility = View.VISIBLE
            statementHandler.tv_call_duration.text = item.duration.toString()
            statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#DFFFE1"))
            statementHandler.tv_lead_status.text = "Connected"
            statementHandler.tv_lead_status.setTextColor(Color.parseColor("#27702C"))
        }


       /* when (item.status!!.id) {
            -1 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
            0 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#47AA4E"))
            5 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FC4F38"))
            6 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FDA73E"))
            7 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#1793D1"))
            else -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
        }*/




    }



    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var user_name: TextView = itemView.findViewById(R.id.user_name)
        var user_mob_no: TextView = itemView.findViewById(R.id.user_mob_no)
        var relll_durr: RelativeLayout = itemView.findViewById(R.id.relll_durr)
        var relll_statuss: RelativeLayout = itemView.findViewById(R.id.relll_statuss)
        var tv_call_duration: TextView = itemView.findViewById(R.id.tv_call_duration)
        var tv_lead_status: TextView = itemView.findViewById(R.id.tv_lead_status)

    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }

}