package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.NameWithImage
import com.exp.clonefieldkonnect.model.ReportcountModel

class ReportAdapter(
    var activity: Activity, var reportArr: MutableList<NameWithImage>,
    var onClickEmail: OnEmailClick,
    var data: ReportcountModel.Data?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_report, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tvTitle.setText(reportArr.get(position).name)
        statementHandler.img_report.setImageResource(reportArr.get(position).imageRes)
        statementHandler.cardreportview.setOnClickListener {
            onClickEmail.onClickEmail(reportArr.get(position).Id)
//            Toast.makeText(activity, "Id==="+reportArr.get(position).Id, Toast.LENGTH_LONG).show()
        }
        if (position == 0){
            statementHandler.img_icon.visibility = View.GONE
            statementHandler.tv_count.visibility = View.GONE
        }else if (position == 1){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pendingAttendance.toString()
        }else if (position == 2){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pendingTourPlan.toString()
        }else if (position == 3){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pendingExpense.toString()
        }else if (position == 4){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pending_orders.toString()
        }else if (position == 5){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pendingOrderDiscount.toString()
        }else if (position == 6){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pendingallexpense.toString()
        }else if (position == 7){
            statementHandler.img_icon.visibility = View.VISIBLE
            statementHandler.tv_count.visibility = View.VISIBLE
            statementHandler.tv_count.text = data!!.pending_appointment.toString()
        }else{
            statementHandler.img_icon.visibility = View.GONE
            statementHandler.tv_count.visibility = View.GONE
        }


       /* statementHandler.tvEmail.tag = position
        statementHandler.tvEmail.setOnClickListener {
            val pos = it.tag as Int
            onClickEmail.onClickEmail(reportArr.get(pos))
        }*/
    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
//        var tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        var img_report: ImageView = itemView.findViewById(R.id.img_report)
        var cardreportview: CardView = itemView.findViewById(R.id.cardreportview)
        var img_icon: ImageView = itemView.findViewById(R.id.img_icon)
        var tv_count: TextView = itemView.findViewById(R.id.tv_count)
    }

    override fun getItemCount(): Int {
        return reportArr.size
    }

    interface OnEmailClick {
        fun onClickEmail(id: Int)
    }
}