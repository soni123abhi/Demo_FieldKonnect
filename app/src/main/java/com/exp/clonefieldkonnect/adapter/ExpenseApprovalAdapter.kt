package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.ExpenseApprovalModel

class ExpenseApprovalAdapter(var activity: Activity, var userexpenselist: ArrayList<ExpenseApprovalModel.Data>, var onClickEmail_expense: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var flag = ""
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_expenseapprovallistlayout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tv_name.text = userexpenselist.get(position).userName.toString()
        statementHandler.tv_expense_num.text = "#"+userexpenselist.get(position).id.toString()
        statementHandler.tv_expense_type.text = userexpenselist.get(position).expensesTypeName.toString()
        statementHandler.tv_expense_date.text = userexpenselist.get(position).date.toString()
        statementHandler.tv_expense_status.text = userexpenselist.get(position).status.toString()



        if (userexpenselist.get(position).expenseImage.size > 0){
            statementHandler.tv_expense_attachment.text = userexpenselist.get(position).expenseImage.size.toString()+" Att."
        }else{
            statementHandler.tv_expense_attachment.text = "No Att."
        }
        statementHandler.rel_main.setOnClickListener {
//            flag = "Create"
            onClickEmail_expense.onClickEmail_expense(userexpenselist[position].id.toString(),userexpenselist.get(position).status.toString()
                ,userexpenselist.get(position).date.toString(),userexpenselist.get(position).self)
        }
        when (userexpenselist[position].status.toString()) {
            "Approved" -> {
                statementHandler.tv_expense_status.setTextColor(Color.parseColor("#00D23B"))
                statementHandler.tv_expense_tag.visibility = View.VISIBLE
                statementHandler.tv_expense_tag2.visibility = View.VISIBLE
                statementHandler.tv_expense_claim_amt.visibility = View.VISIBLE
                statementHandler.tv_expense_tag.text = "Clm Amt."
                statementHandler.tv_expense_tag2.text = "App Amt."
                statementHandler.tv_expense_tag.setTextColor(Color.parseColor("#000000"))
                statementHandler.tv_expense_tag2.setTextColor(Color.parseColor("#00D23B"))
                statementHandler.tv_expense_amount.text = userexpenselist.get(position).claimAmount.toString()
                statementHandler.tv_expense_claim_amt.text = userexpenselist.get(position).approve_amount.toString()
            }
            "Rejected" -> {
                statementHandler.tv_expense_status.setTextColor(Color.parseColor("#FF0000"))
                statementHandler.tv_expense_tag.visibility = View.VISIBLE
                statementHandler.tv_expense_tag2.visibility = View.GONE
                statementHandler.tv_expense_claim_amt.visibility = View.GONE
                statementHandler.tv_expense_tag.text = "Rej Amt."
                statementHandler.tv_expense_tag.setTextColor(Color.parseColor("#FF0000"))
                statementHandler.tv_expense_amount.text = userexpenselist.get(position).claimAmount.toString()
            }
            "Pending" -> {
                statementHandler.tv_expense_status.setTextColor(Color.parseColor("#FFC700"))
                statementHandler.tv_expense_tag.visibility = View.GONE
                statementHandler.tv_expense_tag2.visibility = View.GONE
                statementHandler.tv_expense_claim_amt.visibility = View.GONE
                statementHandler.tv_expense_amount.text = userexpenselist.get(position).claimAmount.toString()
            }
            "Checked" -> {
                statementHandler.tv_expense_status.setTextColor(Color.parseColor("#813F0B"))
                statementHandler.tv_expense_tag.visibility = View.GONE
                statementHandler.tv_expense_tag2.visibility = View.GONE
                statementHandler.tv_expense_claim_amt.visibility = View.GONE
                statementHandler.tv_expense_amount.text = userexpenselist.get(position).claimAmount.toString()
            }
            "Checked By Reporting" -> {
                statementHandler.tv_expense_status.setTextColor(Color.parseColor("#DF8F18"))
                statementHandler.tv_expense_tag.visibility = View.GONE
                statementHandler.tv_expense_tag2.visibility = View.GONE
                statementHandler.tv_expense_claim_amt.visibility = View.GONE
                statementHandler.tv_expense_amount.text = userexpenselist.get(position).approve_amount.toString()
            }
        }


    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var tv_expense_num: TextView = itemView.findViewById(R.id.tv_expense_num)
        var tv_expense_type: TextView = itemView.findViewById(R.id.tv_expense_type)
        var tv_expense_date: TextView = itemView.findViewById(R.id.tv_expense_date)
        var tv_expense_amount: TextView = itemView.findViewById(R.id.tv_expense_amount)
        var tv_expense_attachment: TextView = itemView.findViewById(R.id.tv_expense_attachment)
        var tv_expense_status: TextView = itemView.findViewById(R.id.tv_expense_status)
        var tv_expense_tag: TextView = itemView.findViewById(R.id.tv_expense_tag)
        var tv_expense_tag2: TextView = itemView.findViewById(R.id.tv_expense_tag2)
        var tv_expense_claim_amt: TextView = itemView.findViewById(R.id.tv_expense_claim_amt)
        var rel_main: CardView = itemView.findViewById(R.id.rel_main)
//        var cardbtn_view: CardView = itemView.findViewById(R.id.cardbtn_view)
    }

    override fun getItemCount(): Int {
        return userexpenselist.size
    }


    interface OnEmailClick {
        fun onClickEmail_expense(id: String, status: String, date: String, self: Boolean?)
    }
}