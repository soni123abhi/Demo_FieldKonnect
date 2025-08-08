package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.UserTourListModel

class UserTourUseradapter(var activity: Activity, var useractivitylist: ArrayList<UserTourListModel.Data>, var onClickEmail_tour: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var flag = ""
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_usertourr, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.user_name.setText(useractivitylist.get(position).name)
        statementHandler.cardbtn_view.setOnClickListener {
            flag = "View"
            onClickEmail_tour.onClickEmail_tour(useractivitylist.get(position).userId,flag,useractivitylist.get(position).name)
        }
        statementHandler.cardbtn_create.setOnClickListener {
            flag = "Create"
            onClickEmail_tour.onClickEmail_tour(useractivitylist.get(position).userId, flag, useractivitylist.get(position).name)
        }

    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var user_name: TextView = itemView.findViewById(R.id.user_act_name)
        var cardbtn_create: CardView = itemView.findViewById(R.id.cardbtn_create)
        var cardbtn_view: CardView = itemView.findViewById(R.id.cardbtn_view)
    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClickEmail_tour(id: Int?, date: String?, name: String?)
    }
}