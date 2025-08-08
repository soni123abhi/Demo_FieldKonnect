package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.NotificationModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationAdapter(var activity: Activity, var categoryArr: ArrayList<NotificationModel.Data>, var onClickEmail1: NotificationAdapter.OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notification_listing_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler


        val capitalizedTitle = categoryArr[position].title?.replaceFirstChar { it.uppercaseChar() }
        statementHandler.tvTitle.text = capitalizedTitle

        statementHandler.tvnoti_msg.setText(categoryArr.get(position).body)

        var dt = formatDateTime(categoryArr.get(position).createdAt!!)

        statementHandler.tvnoti_date.setText(dt.toString())

        statementHandler.relativeMain.setOnClickListener {
            onClickEmail1.onClicknotification(categoryArr[position].id,categoryArr[position].model)
        }



    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvTitle: TextView = itemView.findViewById(R.id.tvnoti_tittle)
        var tvnoti_msg: TextView = itemView.findViewById(R.id.tvnoti_msg)
        var tvnoti_date: TextView = itemView.findViewById(R.id.tvnoti_date)
        var relativeMain: RelativeLayout = itemView.findViewById(R.id.relativeMain)
    }

    override fun getItemCount(): Int {
        return categoryArr.size
    }

    interface OnEmailClick {
        fun onClicknotification(id: Int?, model: String?)
    }


    fun formatDateTime(isoString: String): Pair<String, String> {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        isoFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = isoFormat.parse(isoString) ?: return Pair("", "")

        val dateFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())      // Example: 6-8-2025
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())       // Example: 10:34 AM

        dateFormat.timeZone = TimeZone.getDefault()
        timeFormat.timeZone = TimeZone.getDefault()

        return Pair(dateFormat.format(date), timeFormat.format(date))
    }

}