package com.exp.clonefieldkonnect.adapter


import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.model.DealerApprovalListModel
import java.util.ArrayList

class DealerApprovalAdapter(var activity: Activity, var userexpenselist: ArrayList<DealerApprovalListModel.Data>, var onClickEmail_expense: OnEmailClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var flag = ""
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.report_dealeraprovallistlayout, parent, false)
        return StatementHandler(v)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val statementHandler = holder as StatementHandler
        statementHandler.tv_name.text = userexpenselist.get(position).firmName
        statementHandler.tv_appointment_date.text = userexpenselist.get(position).appointmentDate.toString()

        val createdByName = userexpenselist.get(position).createdBy
        if (createdByName != null) {
            statementHandler.tv_created_by.text = "created By : "+createdByName.toString()
        }
        statementHandler.tv_dealar_status.text = userexpenselist.get(position).approvalStatus.toString()

        if (userexpenselist.get(position).certificate!!.isNotEmpty()){
            statementHandler.card_certificate.visibility = View.VISIBLE
        }else{
            statementHandler.card_certificate.visibility = View.INVISIBLE
        }

        statementHandler.card_view.setOnClickListener {
            flag = "view"
            onClickEmail_expense.onClickEmail_expense(userexpenselist[position].id.toString(),flag,userexpenselist.get(position).created_by_id)
        }

        statementHandler.card_edit.setOnClickListener {
            Toast.makeText(activity,"Working on it..!!",Toast.LENGTH_SHORT).show()
        }

        statementHandler.card_certificate.setOnClickListener {
            var link = userexpenselist[position].certificate.toString()
            if (link.isNotEmpty()){
                downloadPdf(link)
            }else{
                Toast.makeText(activity,"Pdf Not available",Toast.LENGTH_SHORT).show()
            }
        }






        when (userexpenselist[position].approvalStatus.toString()) {
            "Approved By Sales Team" -> {
                statementHandler.tv_dealar_status.setTextColor(Color.parseColor("#00D23B"))
            }
            "Rejected" -> {
                statementHandler.tv_dealar_status.setTextColor(Color.parseColor("#FF0000"))
            }
            "Pending" -> {
                statementHandler.tv_dealar_status.setTextColor(Color.parseColor("#FFC700"))
            }
            "Approved By Account" -> {
                statementHandler.tv_dealar_status.setTextColor(Color.parseColor("#813F0B"))
            }
            "Approved By HO" -> {
                statementHandler.tv_dealar_status.setTextColor(Color.parseColor("#DF8F18"))
            }
        }
    }

    private fun downloadPdf( pdfUrl: String?) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Certificate.pdf")
        val downloadManager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(activity,"Successfully downloaded",Toast.LENGTH_SHORT).show()
    }

    private inner class StatementHandler internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var tv_name: TextView = itemView.findViewById(R.id.tv_name)
        var tv_appointment_date: TextView = itemView.findViewById(R.id.tv_appointment_date)
        var tv_dealar_status: TextView = itemView.findViewById(R.id.tv_dealar_status)
        var tv_created_by: TextView = itemView.findViewById(R.id.tv_created_by)
        var card_view: CardView = itemView.findViewById(R.id.card_view)
        var card_edit: CardView = itemView.findViewById(R.id.card_edit)
        var card_certificate: CardView = itemView.findViewById(R.id.card_certificate)
    }

    override fun getItemCount(): Int {
        return userexpenselist.size
    }


    interface OnEmailClick {
        fun onClickEmail_expense(id: String, flag: String, createdById: Int?)
    }
}