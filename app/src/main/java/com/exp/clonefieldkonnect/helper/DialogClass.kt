package com.exp.clonefieldkonnect.helper

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.exp.clonefieldkonnect.R

object
DialogClass {

    fun progressDialog(activity: Activity): ProgressDialog {

        val mProgressDialog = ProgressDialog(activity);

        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        return mProgressDialog

    }


    fun alertDialog(
        title: String,
        description: String,
        activity: Activity,
        isFinishActivity: Boolean
    ) {

        val builder = AlertDialog.Builder(activity)

        builder.setMessage(description)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id ->
                if (isFinishActivity)
                    activity.finish()
            }

        val alert = builder.create()
        //Setting the title manually
        alert.setTitle(title)
        alert.show()
    }

    fun filterDialog(
        activity: Activity?,filterData: FilterData
    ) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_filter)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        val wlp = window!!.attributes
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT

        val tvToday = dialog.findViewById<TextView>(R.id.tvToday)
        val tvMonth = dialog.findViewById<TextView>(R.id.tvMonth)
        val tvLastMonth = dialog.findViewById<TextView>(R.id.tvLastMonth)
        val tvQ1 = dialog.findViewById<TextView>(R.id.tvQ1)
        val tvQ2 = dialog.findViewById<TextView>(R.id.tvQ2)
        val tvQ3 = dialog.findViewById<TextView>(R.id.tvQ3)
        val tvQ4 = dialog.findViewById<TextView>(R.id.tvQ4)
        val tvYTM = dialog.findViewById<TextView>(R.id.tvYTM)
        val tvLastYear = dialog.findViewById<TextView>(R.id.tvLastYear)

        tvToday.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Today")
        }
        tvMonth.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("This Month")
        }
        tvLastMonth.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Last Month")
        }
        tvQ1.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Quarter 1")
        }
        tvQ2.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Quarter 2")
        }
        tvQ3.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Quarter 3")
        }
        tvQ4.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Quarter 4")
        }
        tvYTM.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("YTM")
        }
        tvLastYear.setOnClickListener {
            dialog.dismiss()
            filterData.filterClick("Last Year")
        }

        dialog.show()
    }

    interface FilterData{
        fun filterClick(value :String)
    }
}