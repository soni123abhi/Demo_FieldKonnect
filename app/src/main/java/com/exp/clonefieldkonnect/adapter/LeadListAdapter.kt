package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.LeadModel

class LeadListAdapter(var activity: Activity, var useractivitylist: ArrayList<LeadModel.Data_lead>, var onClickEmail1: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.lead_listing_layout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        val item = useractivitylist[position]

        statementHandler.tvcompany_name.text = item.name.toString()
        statementHandler.tvopppp.text = item.opportunity_status.orEmpty()
        statementHandler.tv_lead_status.text = item.status!!.displayName.orEmpty()
        statementHandler.tvuser_name.text = item.contact!!.name.orEmpty()
        statementHandler.tvmobile.text = item.contact!!.phoneNumber.orEmpty()
        statementHandler.tvaddress.text = item.city.orEmpty()
        statementHandler.tvopportunity.text = item.contact!!.leadSource.orEmpty()

//        val plainText = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Html.fromHtml(item.note ?: "", Html.FROM_HTML_MODE_COMPACT).toString()
//        } else {
//            Html.fromHtml(item.note ?: "").toString()
//        }
        if (item.note.isNullOrEmpty()){
        }else{
            statementHandler.tvuser_notee.text = item.note.toString()
        }


        when (item.status!!.id) {
            -1 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
            0 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#47AA4E"))
            5 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FC4F38"))
            6 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#FDA73E"))
            7 -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#1793D1"))
            else -> statementHandler.relll_statuss.setBackgroundColor(Color.parseColor("#182D69"))
        }

        statementHandler.img_view.setOnClickListener {
            onClickEmail1.onClicklead(item.id)
        }
        
        statementHandler.img_call.setOnClickListener { 
            if (!item.contact!!.phoneNumber.isNullOrEmpty()){
                opencalldialog(item.contact!!.phoneNumber)
            }
        }
        statementHandler.img_msg.setOnClickListener { 
            if (!item.contact!!.phoneNumber.isNullOrEmpty()){
                openEmailDialog(item.contact!!.phoneNumber)
            }
        }
        statementHandler.img_whatsapp.setOnClickListener { 
            if (!item.contact!!.phoneNumber.isNullOrEmpty()){
                openwhatsappdialog(item.contact!!.phoneNumber)
            }
        }
        statementHandler.img_location.setOnClickListener {
            if (!item.address!!.isNullOrEmpty()){
                openmapdialog(item.address!!)
            }
        }

    }

    private fun openmapdialog(address: String) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Google Maps not installed.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun openwhatsappdialog(phoneNumber: String?) {
        phoneNumber?.let {
            val formattedNumber = it.replace("+91", "").replace(" ", "").trim()
            val url = "https://wa.me/91$formattedNumber" // Replace "91" with correct country code
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            intent.setPackage("com.whatsapp")
            try {
                activity.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                try {
                    val playStoreIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.whatsapp")
                    )
                    activity.startActivity(playStoreIntent)
                } catch (e: ActivityNotFoundException) {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp")
                    )
                    activity.startActivity(browserIntent)
                }
            }
        } ?: run {
            Toast.makeText(activity, "Phone number not available", Toast.LENGTH_SHORT).show()
        }
    }



    private fun openEmailDialog(emailAddress: String?) {
        emailAddress?.let {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$it") // only email apps should handle this
                putExtra(Intent.EXTRA_SUBJECT, "Subject here") // optional
                putExtra(Intent.EXTRA_TEXT, "Body here") // optional
            }

            try {
                activity.startActivity(Intent.createChooser(intent, "Send email..."))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(activity, "No email app installed.", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(activity, "Email address not available", Toast.LENGTH_SHORT).show()
        }
    }



    private fun opencalldialog(phoneNumber: String?) {
        phoneNumber?.let {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$it")
            activity.startActivity(intent)
        } ?: run {
            Toast.makeText(activity, "Phone number not available", Toast.LENGTH_SHORT).show()
        }
    }


    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tvcompany_name: TextView = itemView.findViewById(R.id.tvcompany_name)
        var tvopppp: TextView = itemView.findViewById(R.id.tvopppp)
        var tv_lead_status: TextView = itemView.findViewById(R.id.tv_lead_status)
        var tvuser_name: TextView = itemView.findViewById(R.id.tvuser_name)
        var tvmobile: TextView = itemView.findViewById(R.id.tvmobile)
        var tvaddress: TextView = itemView.findViewById(R.id.tvaddress)
        var tvopportunity: TextView = itemView.findViewById(R.id.tvopportunity)
        var tvuser_notee: TextView = itemView.findViewById(R.id.tvuser_notee)
        var relll_statuss: RelativeLayout = itemView.findViewById(R.id.relll_statuss)

        var img_view: ImageView = itemView.findViewById(R.id.img_view)
        var img_call: ImageView = itemView.findViewById(R.id.img_call)
        var img_msg: ImageView = itemView.findViewById(R.id.img_msg)
        var img_whatsapp: ImageView = itemView.findViewById(R.id.img_whatsapp)
        var img_location: ImageView = itemView.findViewById(R.id.img_location)
    }

    override fun getItemCount(): Int {
        return useractivitylist.size
    }


    interface OnEmailClick {
        fun onClicklead(id: Int?)
    }
}