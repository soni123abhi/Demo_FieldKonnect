package com.exp.clonefieldkonnect.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import com.exp.clonefieldkonnect.R
import com.exp.clonefieldkonnect.connection.APIResultLitener
import com.exp.clonefieldkonnect.connection.ApiClient
import com.exp.clonefieldkonnect.model.AttendanceSubmitModel
import com.exp.clonefieldkonnect.model.LastCallRemarkModel
import com.exp.import.Utilities
import org.json.JSONObject
import retrofit2.Response

class MyApplication : Application() {

    private var activeActivityCount = 0 // Track foreground/background transitions
    var leadstatuslist: ArrayList<LastCallRemarkModel.AllTypes> = ArrayList()
    var leadstatusname : ArrayList<String> = ArrayList()
    var leadstatusid : ArrayList<String> = ArrayList()
    var lead_id = ""
    var last_call_id = ""
    private var currentActivity: Activity? = null

    var lastCallRemarkListener: LastCallRemarkListener? = null

    companion object {
        private var instance: MyApplication? = null
        fun getInstance(): MyApplication = instance!!
    }


    override fun onCreate() {
        super.onCreate()
        instance = this

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                // Perform all security validations here
//                SecurityValidator.checkEnvironmentAndBlock(activity)
                currentActivity = activity

                // Call API only if this Activity is the first resumed
                if (StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activity).toString().isNotEmpty()){
                    getlastcallremark(activity)
                }

            }

            override fun onActivityPaused(activity: Activity) {
                if (currentActivity == activity) currentActivity = null

            }
            override fun onActivityStarted(activity: Activity) {
            }
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityStopped(activity: Activity) {
            }
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        })
    }

    private fun submitRemarkApiCall(remark: String, lastCallId: String, leadId: String, activity: Activity) {
        if (!Utilities.isOnline(activity)) {
            return
        }
        val queryParams = java.util.HashMap<String, String>()
        queryParams["id"] = lastCallId
        queryParams["lead_type_id"] = leadId
        queryParams["remark"] = remark

        ApiClient.submitcallremark(
            StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activity).toString(),
            queryParams,
            object : APIResultLitener<AttendanceSubmitModel> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onAPIResult(response: Response<AttendanceSubmitModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            Toast.makeText(activity,"Submitted Successfully !!",Toast.LENGTH_SHORT).show()
                        } else {
                            val jsonObject: JSONObject
                            try {
                                jsonObject = JSONObject(response.errorBody()!!.string())

                                DialogClass.alertDialog(
                                    jsonObject.getString("status"),
                                    jsonObject.getString("message"),
                                    activity, false
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    else {
                        Toast.makeText(activity, resources.getString(R.string.data_not_found), Toast.LENGTH_LONG).show()
                    }
                }
            })
    }


    private fun getlastcallremark(activity: Activity) {
        if (!Utilities.isOnline(activity)) {
            return
        }
        ApiClient.getLastcallremark(StaticSharedpreference.getInfo(Constant.ACCESS_TOKEN, activity).toString(),
            object : APIResultLitener<LastCallRemarkModel> {
                override fun onAPIResult(response: Response<LastCallRemarkModel>?, errorMessage: String?) {
                    if (response != null && errorMessage == null) {
                        if (response.code() == 200) {
                            println("DATAA=aa="+response.body()!!.data!!.lastCallRemark)
                            if (response.body()!!.success == true){
                                val data = response.body()?.data
                                if (data?.lastCallRemark == false) {
                                    // Notify listener (Activity will show popup)
                                    leadstatuslist.clear()
                                    leadstatuslist.addAll(response.body()!!.data!!.allTypes)

                                    for (item in leadstatuslist) {
                                        val name = item.displayName.toString()
                                        val id = item.id.toString()

                                        if (!leadstatusname.contains(name)) {
                                            leadstatusname.add(name)
                                            leadstatusid.add(id)
                                        }
                                    }
                                    activity.runOnUiThread {
                                        // Notify the listener (MainActivity)
                                        lastCallRemarkListener?.onLastCallRemarkRequired(data)
                                    }
                                }
                            }
                        }
                        else {
                            val jsonObject: JSONObject
                            jsonObject = JSONObject(response.errorBody()!!.string())
                            DialogClass.alertDialog(
                                jsonObject.getString("status"),
                                jsonObject.getString("message"),
                                activity, false
                            )
                        }
                    }
                }
            })
    }



    // Global method to show popup (reusable)
    fun showRemarkPopupGlobal(data: LastCallRemarkModel.Data, activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.popup_call_remark_layout, null)
        builder.setCancelable(false)

        val edtleadtype: AutoCompleteTextView = view.findViewById(R.id.edtleadtype)
        val edtcallremark: AutoCompleteTextView = view.findViewById(R.id.edtcallremark)
        val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)

        edtleadtype.setText(data!!.leadType.toString())
        lead_id = data!!.leadTypeId.toString()
        last_call_id = data!!.lastCallId.toString()

        edtleadtype.setOnClickListener {
            spinnerleadstatus(edtleadtype,activity)
        }

        val dialog = builder.setView(view).create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        cardSubmit.setOnClickListener {
            val remark = edtcallremark.text.toString()
            if (remark.isEmpty()) {
                Toast.makeText(activity, "Please Enter Remark", Toast.LENGTH_SHORT).show()
            } else {
                // Submit API
                println("SSSSSSSS=="+last_call_id+"<<"+lead_id+"<<"+remark)
                submitRemarkApiCall(remark, last_call_id,lead_id, activity)
                dialog.dismiss()
            }
        }

        // Block back button
        dialog.setOnKeyListener { _, keyCode, _ -> keyCode == android.view.KeyEvent.KEYCODE_BACK }
    }



    /* @SuppressLint("MissingInflatedId")
     private lateinit var alertDialog: AlertDialog

     @SuppressLint("MissingInflatedId")
     private fun showremarkpopup(data: LastCallRemarkModel.Data?, activity: Activity) {
         val builder = androidx.appcompat.app.AlertDialog.Builder(activity) // ✅ FIXED
         val inflater = activity.layoutInflater                              // ✅ FIXED
         val view = inflater.inflate(R.layout.popup_call_remark_layout, null)
         builder.setCancelable(false)

         val edtleadtype: AutoCompleteTextView = view.findViewById(R.id.edtleadtype)
         val edtcallremark: AutoCompleteTextView = view.findViewById(R.id.edtcallremark)
         val cardSubmit: CardView = view.findViewById(R.id.cardSubmit)

         if (data!= null){
             edtleadtype.setText(data!!.leadType.toString())
             lead_id = data!!.leadTypeId.toString()
             last_call_id = data!!.lastCallId.toString()
         }

         edtleadtype.setOnClickListener {
             spinnerleadstatus(edtleadtype,activity)
         }


         cardSubmit.setOnClickListener {
             if (edtcallremark.text.toString().isNullOrEmpty()){
                 Toast.makeText(activity,"Please Enter Remark",Toast.LENGTH_SHORT).show()
             }else{
                 println("DDDDDDDDDD=="+lead_id+"<<"+edtcallremark.text.toString()+"<<"+last_call_id)
 //                submitremark(edtcallremark.text.toString(), "","")
             }
         }


         builder.setView(view)
         alertDialog = builder.create()
         alertDialog.show()

     }
 */

    private fun spinnerleadstatus(edtstatus: AutoCompleteTextView, activity: Activity) {
        val builder = android.app.AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog2, null)
        builder.setView(dialogView)

        val searchEditText = dialogView.findViewById<EditText>(R.id.searchEditText)
        val listView = dialogView.findViewById<ListView>(R.id.listView)
        val button = dialogView.findViewById<Button>(R.id.button)

        val colorsArray = leadstatusname.map { it.toString() }.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, colorsArray)
        listView.adapter = adapter

        builder.setTitle("Select Status")

        val dialog = builder.create()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter.filter(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        button.setOnClickListener {
            edtstatus.setText("")
            lead_id = ""
            dialog.dismiss()
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedPosition = leadstatusname.indexOf(adapter.getItem(position).toString())
            if (selectedPosition != -1) {
                val selectedParentId = leadstatusid[selectedPosition].toString()
                val selectedParentName = leadstatusname[selectedPosition].toString()

                edtstatus.setText(selectedParentName)
                lead_id = selectedParentId

                println("Abhi=id=$lead_id")


                dialog.dismiss()
            }
        }

        dialog.show() // Show the dialog
    }




}
