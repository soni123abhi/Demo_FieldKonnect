package com.exp.clonefieldkonnect.adapter

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.exp.clonefieldkonnect.R
import java.io.File

class ExpenseImagePathEditAdapter(
    var activityLocal: Activity,
    var selected_image_path2: ArrayList<String>,
    var onClickEmail1: OnEmailClick,
    var imageId: ArrayList<Int>
) : ArrayAdapter<String>(activityLocal, R.layout.item_image_path_expense, selected_image_path2) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val path = getItem(position) ?: ""

        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_image_path_expense_view, parent, false)

        val imageViewFile: ImageView = view.findViewById(R.id.imageViewFile)
        val buttonRemoveedit: ImageView = view.findViewById(R.id.buttonRemoveedit)
        val imageViewdoewnload: ImageView = view.findViewById(R.id.imageViewdoewnload)
        val textViewPath: TextView = view.findViewById(R.id.textViewPath)

        val file = File(path)
        if (file.extension.equals("pdf", ignoreCase = true)) {
            imageViewFile.setImageResource(R.drawable.ic_attachmnet_icon)
        } else {
            loadImageIntoImageView(file, imageViewFile)
        }
        imageViewdoewnload.setOnClickListener {
            val link = selected_image_path2[position]
            downloadPdf(activityLocal,link)
        }
        imageViewFile.setOnClickListener {
            onClickEmail1.onClickEmail1(imageViewFile,textViewPath,position,selected_image_path2)
        }
        buttonRemoveedit.setOnClickListener {
            Toast.makeText(activityLocal,"Successfully Removed",Toast.LENGTH_SHORT).show()
            onClickEmail1.onClickEmail2(imageId[position])
            selected_image_path2.removeAt(position)
            imageId.removeAt(position)
            notifyDataSetChanged()
        }

        textViewPath.text = file.name

        return view
    }

    private fun downloadPdf(activityLocal: Activity, pdfUrl: String?) {
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Attachment")
        val downloadManager = activityLocal.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
        Toast.makeText(activityLocal,"Successfully downloaded",Toast.LENGTH_SHORT).show()
    }

    private fun loadImageIntoImageView(file: File, imageView: ImageView) {
        Glide.with(activityLocal)
            .load(file)
            .placeholder(R.drawable.ic_attachmnet_icon)
            .into(imageView)
    }
    interface OnEmailClick {
        fun onClickEmail1(imageViewFile: ImageView, textViewPath: TextView, position: Int, selected_image_path2: ArrayList<String>)

        fun onClickEmail2(selectedImagePath2: Int)
    }

}
