package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.LeadDetailModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class LeadActivityLeadAdapter(var activity: Activity, var useractivitylist: ArrayList<LeadDetailModel.NotesTasks>,var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    private var lastDisplayedDate: String? = null // Declare this in your Adapter


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_activity_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]
        val currentDate = item.createdAtFormatted.toString()

        val timeString = extractTimeWithAmPm(item.createdAt.toString())
        statementHandler.tv_time.text = timeString.toString()

        statementHandler.tv_headd.text = item.type
            ?.lowercase()
            ?.replaceFirstChar { it.uppercase() }

        holder.tv_created_by.text = item.createdby!!.name.toString()


        statementHandler.img_option.setOnClickListener {
            val popupMenu = PopupMenu(it.context, it)
            popupMenu.menu.add("Edit")
            popupMenu.menu.add("Cancel")

            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.title) {
                    "Edit" -> {
                        onClickEmail1.onClickactivityedit(item.id,item.type.toString(),useractivitylist[position])
                        true
                    }
                    "Cancel" -> {
                        popupMenu.dismiss()
                        true
                    }
                    else -> false
                }
            }

            popupMenu.show()
        }



        if (currentDate == lastDisplayedDate) {
            holder.linearvisit.visibility = View.GONE
        } else {
            holder.linearvisit.visibility = View.VISIBLE
            holder.tvcompany_date.text = currentDate
            lastDisplayedDate = currentDate
        }
        if (item.type.equals("task")){
            holder.card_status.visibility = View.VISIBLE
            holder.tv_created_by.visibility = View.VISIBLE

            holder.tv_lead_status.text = item.priority
            holder.tv_msg.text = item.description +"("+item.date+","+item.time+")"

            ViewCompat.setBackgroundTintList(holder.rel_image, ColorStateList.valueOf(Color.parseColor("#5EB3B2")))
            holder.img_taskk.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_lead_activity_task))


        }else if (item.type.equals("note")){
            holder.card_status.visibility = View.GONE
            holder.tv_created_by.visibility = View.VISIBLE


           /* val spanned: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(item.note ?: "", Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(item.note ?: "")
            }*/
            holder.tv_msg.text = item.note.toString()


            ViewCompat.setBackgroundTintList(holder.rel_image, ColorStateList.valueOf(Color.parseColor("#CAF4C6")))
            holder.img_taskk.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_lead_activity_note))

        }else if (item.type.equals("log")){
            holder.tv_created_by.visibility = View.GONE
            holder.card_status.visibility = View.GONE

            holder.tv_msg.text = item.message.toString()

            ViewCompat.setBackgroundTintList(holder.rel_image, ColorStateList.valueOf(Color.parseColor("#E4DDFE")))
            holder.img_taskk.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_lead_log))

        }

    }

    fun extractTimeWithAmPm(input: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure correct time zone if needed
            val date = inputFormat.parse(input)

            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault()) // 12-hour format with AM/PM
            outputFormat.format(date!!)
        } catch (e: Exception) {
            ""
        }
    }



    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvcompany_date: TextView = itemView.findViewById(R.id.tvcompany_date)
        var linearvisit: LinearLayout = itemView.findViewById(R.id.linearvisit)
        var tv_headd: TextView = itemView.findViewById(R.id.tv_headd)
        var tv_lead_status: TextView = itemView.findViewById(R.id.tv_lead_status)
        var tv_created_by: TextView = itemView.findViewById(R.id.tv_created_by)
        var tv_msg: TextView = itemView.findViewById(R.id.tv_msg)
        var tv_time: TextView = itemView.findViewById(R.id.tv_time)
        var rel_image: CardView = itemView.findViewById(R.id.rel_image)
        var card_status: CardView = itemView.findViewById(R.id.card_status)
        var img_taskk: ImageView = itemView.findViewById(R.id.img_taskk)
        var img_option: ImageView = itemView.findViewById(R.id.img_option)
    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }

    interface OnEmailClick {
        fun onClickactivityedit(id: Int?, type: String, notesTasks: LeadDetailModel.NotesTasks)
    }

}