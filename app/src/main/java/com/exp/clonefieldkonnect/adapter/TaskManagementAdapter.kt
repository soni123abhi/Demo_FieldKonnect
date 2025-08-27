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
import com.exp.clonefieldkonnect.model.TaskManagemnetModel

class TaskManagementAdapter(var activity: Activity, var useractivitylist: ArrayList<TaskManagemnetModel.Data>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_task_managment_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        var name = item.taskType?.replaceFirstChar { it.uppercase() } ?: ""
        var project_name = ""

        if (item.taskType.equals("lead")){
            project_name = item.lead!!.companyname.toString()
        }else if (item.taskType.equals("project")){
            project_name = item.project!!.name.toString()
        }else if (item.taskType.equals("customer")){
            project_name = item.customers!!.name.toString()
        }
        if (project_name.isNotEmpty()){
            statementHandler.tvtask_type.text = name+" :- "+project_name
        }else{
            statementHandler.tvtask_type.text = name
        }

        statementHandler.tvaddress.text = item.users!!.name.orEmpty()
        statementHandler.tvdate_time.text = item.dueDatetime.orEmpty()
        statementHandler.tv_closedate.text = item.completedAt.orEmpty()
        statementHandler.tv_lead_status.text = item.taskStatus.orEmpty()
        statementHandler.tvuser_notee.text = item.descriptions.toString()
        statementHandler.tvtask_title.text = item.title.toString()
        statementHandler.tvdept_task.text = item.taskdepartment!!.name.toString()
        statementHandler.tv_priority.text = item.taskpriority!!.name.toString()



        when (item.taskStatus) {
            "Pending" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FDA73E"))
            "Open" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#1793D1"))
            "In Progress" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
            "Completed" -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#47AA4E"))
            else -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
        }

        statementHandler.relll_statuss.setOnClickListener {
            onClickEmail1.onClicktaskmanagement(item.id,item.taskStatus)
        }


    }




    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvtask_type: TextView = itemView.findViewById(R.id.tvtask_type)
        var tvaddress: TextView = itemView.findViewById(R.id.tvaddress)
        var tv_lead_status: TextView = itemView.findViewById(R.id.tv_lead_status)
        var tvdate_time: TextView = itemView.findViewById(R.id.tvdate_time)
        var tv_closedate: TextView = itemView.findViewById(R.id.tv_closedate)
        var tvuser_notee: TextView = itemView.findViewById(R.id.tvuser_notee)
        var tvtask_title: TextView = itemView.findViewById(R.id.tvtask_title)
        var tvdept_task: TextView = itemView.findViewById(R.id.tvdept_task)
        var tv_priority: TextView = itemView.findViewById(R.id.tv_priority)
        var relll_statuss: RelativeLayout = itemView.findViewById(R.id.relll_statuss)

    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClicktaskmanagement(id: Int?, status: String?)
    }
}