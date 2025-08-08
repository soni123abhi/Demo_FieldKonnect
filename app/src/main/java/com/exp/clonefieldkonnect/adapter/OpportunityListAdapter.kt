package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.OportunityDetailModel

class OpportunityListAdapter(var activity: Activity, var useractivitylist: ArrayList<OportunityDetailModel.Opportunities>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.opportunity_listing_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        statementHandler.tvcompany_name.text = item.lead!!.companyName.toString()
        statementHandler.tv_note.text = item.note.orEmpty()
        statementHandler.tv_price.text = "₹"+item.amount.toString()
        statementHandler.tv_date.text = item.confidence.toString()+"% on "+item.estimatedCloseDate.toString()
        statementHandler.tv_assign.text = "Assigned to "+ item.assignUser!!.name.toString()

        var tag = ""
        statementHandler.img_edit.setOnClickListener {
            tag = "edit"
            onClickEmail1.onClickopportunity(item.id,tag,item)
        }

        statementHandler.img_delete.setOnClickListener {
            tag = "delete"
            onClickEmail1.onClickopportunity(item.id, tag, item)
        }

    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvcompany_name: TextView = itemView.findViewById(R.id.tvcompany_name)
        var tv_note: TextView = itemView.findViewById(R.id.tv_note)
        var tv_price: TextView = itemView.findViewById(R.id.tv_price)
        var tv_date: TextView = itemView.findViewById(R.id.tv_date)
        var tv_assign: TextView = itemView.findViewById(R.id.tv_assign)
        var img_edit: ImageView = itemView.findViewById(R.id.img_edit)
        var img_delete: ImageView = itemView.findViewById(R.id.img_delete)

    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClickopportunity(id: Int?, tag: String, item: OportunityDetailModel.Opportunities)
    }
}