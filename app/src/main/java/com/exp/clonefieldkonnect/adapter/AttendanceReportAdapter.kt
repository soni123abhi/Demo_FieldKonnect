package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.UserAttendanceListModel

class AttendanceReportAdapter(var activity: Activity, var userattendancelist: ArrayList<UserAttendanceListModel.Data>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private val selectedIds = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_attendance, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.user_name.setText(userattendancelist.get(position).name)
        statementHandler.user_date.setText(userattendancelist.get(position).date)
        statementHandler.tvpunchin.setText(userattendancelist.get(position).punchIn)
        statementHandler.tvpunchout.setText(userattendancelist.get(position).punchOut)
        statementHandler.tv_status.setText(userattendancelist.get(position).status)

        println("IDDDDd==="+userattendancelist.get(position).self)

        when (userattendancelist[position].status) {
            "Approve" -> {
                statementHandler.tv_status.setTextColor(Color.parseColor("#00D23B"))
                statementHandler.checkBox.visibility = View.GONE
            }
            "Rejected" -> {
                statementHandler.tv_status.setTextColor(Color.parseColor("#FF0000"))
                statementHandler.checkBox.visibility = View.GONE
            }
            "Pending" -> {
                statementHandler.tv_status.setTextColor(Color.parseColor("#FFC700"))
                if (userattendancelist.get(position).self != true){
                    statementHandler.checkBox.visibility = View.VISIBLE
                }else{
                    statementHandler.checkBox.visibility = View.GONE
                }
            }
        }

        statementHandler.checkBox.setOnCheckedChangeListener(null) // Remove previous listener
        statementHandler.checkBox.isChecked = userattendancelist[position].isChecked // Set initial state
        statementHandler.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val id = userattendancelist[position].attendanceId?.toInt()
            if (id != null) {
                if (isChecked) {
                    if (!selectedIds.contains(id)) {
                        selectedIds.add(id) // Add to selected list
                        userattendancelist[position].isChecked = true // Update state in the list
                        println("selectedIds 1: $selectedIds")
                        onClickEmail1.onClickEmail2(selectedIds)
                    }
                } else {
                    selectedIds.remove(id)
                    userattendancelist[position].isChecked = false // Update state in the list
                    println("selectedIds: $selectedIds")
                    onClickEmail1.onClickEmail2(selectedIds)
                }
                notifyDataSetChanged() // Update the RecyclerView to reflect changes
            }
        }
/*
        if (userattendancelist.get(position).self == true){
            statementHandler.report_attendance_view.visibility = View.GONE
        }else{
            statementHandler.report_attendance_view.visibility = View.VISIBLE
        }
*/

        statementHandler.report_attendance_view.setOnClickListener {
            /*if (userattendancelist.get(position).status == "Approve") {
                Toast.makeText(activity, "Already Approved !!", Toast.LENGTH_SHORT).show()
            } else {*/
            onClickEmail1.onClickEmail1(userattendancelist.get(position).attendanceId!!.toInt(),userattendancelist.get(position).self,
                userattendancelist.get(position).status,selectedIds)
            /*}*/
        }
    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var user_name: TextView = itemView.findViewById(R.id.user_name)
        var user_date: TextView = itemView.findViewById(R.id.user_date)
        var tvpunchin: TextView = itemView.findViewById(R.id.tvpunchin)
        var tvpunchout: TextView = itemView.findViewById(R.id.tvpunchout)
        var tv_status: TextView = itemView.findViewById(R.id.tv_status)
        var report_attendance_view: ImageView = itemView.findViewById(R.id.report_attendance_view)
        var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun getItemCount(): Int {
        return userattendancelist.size
    }

    interface OnEmailClick {
        fun onClickEmail1(id: Int, self: Boolean?, status: String?,selectedIds : MutableList<Int>)
        fun onClickEmail2(selectedIds : MutableList<Int>)
    }
}