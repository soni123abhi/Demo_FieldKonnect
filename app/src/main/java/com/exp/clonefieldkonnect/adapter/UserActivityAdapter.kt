package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.UserActivityListModel

class UserActivityAdapter(var activity: Activity, var useractivitylist: ArrayList<UserActivityListModel.Data>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_useractivity, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.user_name.setText(useractivitylist.get(position).name)
        statementHandler.user_date.setText(useractivitylist.get(position).date)
        statementHandler.cardbtn_view.setOnClickListener {
            onClickEmail1.onClickEmail1(useractivitylist.get(position).userId,useractivitylist.get(position).date)
        }

    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var user_name: TextView = itemView.findViewById(R.id.user_act_name)
        var user_date: TextView = itemView.findViewById(R.id.user_act_date)
        var cardbtn_view: CardView = itemView.findViewById(R.id.cardbtn_view)
    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClickEmail1(id: Int?, date: String?)
    }
}