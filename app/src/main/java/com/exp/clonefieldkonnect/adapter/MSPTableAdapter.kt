package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.Activities
import com.exp.clonefieldkonnect.model.Dataaaaa

class MSPTableAdapter(
    var activity: Activity,
    var tablelist: ArrayList<Dataaaaa>,
    var tableactivitylist: ArrayList<Activities>,) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_msp_report_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler


        println("dddd=="+tablelist.size)
        println("dddd=aaa="+tableactivitylist.size)
        println("dddd=mmmm="+tablelist[0].april[position].activityName)


        statementHandler.title_type.text = tablelist[0].april[position].activityName.toString()

        if (tablelist[0].april!!.isNotEmpty()){
            statementHandler.title_april_participant_value.text = tablelist[0].april[position].totalParticipants.toString()
            statementHandler.title_april_activity_value.text = tablelist[0].april[position].totalPerformed.toString()
        }else{
            statementHandler.title_april_participant_value.text = "-"
            statementHandler.title_april_activity_value.text = "-"
        }

        if (tablelist[0].may!!.isNotEmpty()){
            statementHandler.title_may_participant_value.text = tablelist[0].may[position].totalParticipants.toString()
            statementHandler.title_may_activity_value.text = tablelist[0].may[position].totalPerformed.toString()
        }else{
            statementHandler.title_may_participant_value.text = "-"
            statementHandler.title_may_activity_value.text = "-"
        }

        if (tablelist[0].june!!.isNotEmpty()){
            statementHandler.title_june_participant_value.text = tablelist[0].june[position].totalParticipants.toString()
            statementHandler.title_june_activity_value.text = tablelist[0].june[position].totalPerformed.toString()
        }else{
            statementHandler.title_june_participant_value.text = "-"
            statementHandler.title_june_activity_value.text = "-"
        }

        if (tablelist[0].july!!.isNotEmpty()){
            statementHandler.title_july_participant_value.text = tablelist[0].july[position].totalParticipants.toString()
            statementHandler.title_july_activity_value.text = tablelist[0].july[position].totalPerformed.toString()
        }else{
            statementHandler.title_july_participant_value.text = "-"
            statementHandler.title_july_activity_value.text = "-"
        }

        if (tablelist[0].augest!!.isNotEmpty()){
            statementHandler.title_aug_participant_value.text = tablelist[0].augest[position].totalParticipants.toString()
            statementHandler.title_aug_activity_value.text = tablelist[0].augest[position].totalPerformed.toString()
        }else{
            statementHandler.title_aug_participant_value.text = "-"
            statementHandler.title_aug_activity_value.text = "-"
        }
        if (tablelist[0].sep!!.isNotEmpty()){
            statementHandler.title_sep_participant_value.text = tablelist[0].sep[position].totalParticipants.toString()
            statementHandler.title_sep_activity_value.text = tablelist[0].sep[position].totalPerformed.toString()
        }else{
            statementHandler.title_sep_participant_value.text = "-"
            statementHandler.title_sep_activity_value.text = "-"
        }
        if (tablelist[0].oct!!.isNotEmpty()){
            statementHandler.title_oct_participant_value.text = tablelist[0].oct[position].totalParticipants.toString()
            statementHandler.title_oct_activity_value.text = tablelist[0].oct[position].totalPerformed.toString()
        }else{
            statementHandler.title_oct_participant_value.text = "-"
            statementHandler.title_oct_activity_value.text = "-"
        }
        if (tablelist[0].nov!!.isNotEmpty()){
            statementHandler.title_nov_participant_value.text = tablelist[0].nov[position].totalParticipants.toString()
            statementHandler.title_nov_activity_value.text = tablelist[0].nov[position].totalPerformed.toString()
        }else{
            statementHandler.title_nov_participant_value.text = "-"
            statementHandler.title_nov_activity_value.text = "-"
        }
        if (tablelist[0].dec!!.isNotEmpty()){
            statementHandler.title_dec_participant_value.text = tablelist[0].dec[position].totalParticipants.toString()
            statementHandler.title_dec_activity_value.text = tablelist[0].dec[position].totalPerformed.toString()
        }else{
            statementHandler.title_dec_participant_value.text = "-"
            statementHandler.title_dec_activity_value.text = "-"
        }
        if (tablelist[0].jan!!.isNotEmpty()){
            statementHandler.title_jan_participant_value.text = tablelist[0].jan[position].totalParticipants.toString()
            statementHandler.title_jan_activity_value.text = tablelist[0].jan[position].totalPerformed.toString()
        }else{
            statementHandler.title_jan_participant_value.text = "-"
            statementHandler.title_jan_activity_value.text = "-"
        }
        if (tablelist[0].feb!!.isNotEmpty()){
            statementHandler.title_feb_participant_value.text = tablelist[0].feb[position].totalParticipants.toString()
            statementHandler.title_feb_activity_value.text = tablelist[0].feb[position].totalPerformed.toString()
        }else{
            statementHandler.title_feb_participant_value.text = "-"
            statementHandler.title_feb_activity_value.text = "-"
        }
        if (tablelist[0].mar!!.isNotEmpty()){
            statementHandler.title_march_participant_value.text = tablelist[0].mar[position].totalParticipants.toString()
            statementHandler.title_march_activity_value.text = tablelist[0].mar[position].totalPerformed.toString()
        }else{
            statementHandler.title_march_participant_value.text = "-"
            statementHandler.title_march_activity_value.text = "-"
        }

    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var title_type: TextView = itemView.findViewById(R.id.title_type)
        var title_april_participant_value: TextView = itemView.findViewById(R.id.title_april_participant_value)
        var title_april_activity_value: TextView = itemView.findViewById(R.id.title_april_activity_value)
        var title_may_participant_value: TextView = itemView.findViewById(R.id.title_may_participant_value)
        var title_may_activity_value: TextView = itemView.findViewById(R.id.title_may_activity_value)
        var title_june_participant_value: TextView = itemView.findViewById(R.id.title_june_participant_value)
        var title_june_activity_value: TextView = itemView.findViewById(R.id.title_june_activity_value)
        var title_july_participant_value: TextView = itemView.findViewById(R.id.title_july_participant_value)
        var title_july_activity_value: TextView = itemView.findViewById(R.id.title_july_activity_value)
        var title_aug_participant_value: TextView = itemView.findViewById(R.id.title_aug_participant_value)
        var title_aug_activity_value: TextView = itemView.findViewById(R.id.title_aug_activity_value)
        var title_sep_participant_value: TextView = itemView.findViewById(R.id.title_sep_participant_value)
        var title_sep_activity_value: TextView = itemView.findViewById(R.id.title_sep_activity_value)
        var title_oct_participant_value: TextView = itemView.findViewById(R.id.title_oct_participant_value)
        var title_oct_activity_value: TextView = itemView.findViewById(R.id.title_oct_activity_value)
        var title_nov_participant_value: TextView = itemView.findViewById(R.id.title_nov_participant_value)
        var title_nov_activity_value: TextView = itemView.findViewById(R.id.title_nov_activity_value)
        var title_dec_participant_value: TextView = itemView.findViewById(R.id.title_dec_participant_value)
        var title_dec_activity_value: TextView = itemView.findViewById(R.id.title_dec_activity_value)
        var title_jan_participant_value: TextView = itemView.findViewById(R.id.title_jan_participant_value)
        var title_jan_activity_value: TextView = itemView.findViewById(R.id.title_jan_activity_value)
        var title_feb_participant_value: TextView = itemView.findViewById(R.id.title_feb_participant_value)
        var title_feb_activity_value: TextView = itemView.findViewById(R.id.title_feb_activity_value)
        var title_march_participant_value: TextView = itemView.findViewById(R.id.title_march_participant_value)
        var title_march_activity_value: TextView = itemView.findViewById(R.id.title_march_activity_value)
    }

    override fun getItemCount(): Int {
        return tableactivitylist.size
    }



}